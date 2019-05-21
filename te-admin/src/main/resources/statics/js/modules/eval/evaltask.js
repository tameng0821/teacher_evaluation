$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'eval/evaltask/list',
        datatype: "json",
        colModel: [
            {label: '序号', name: 'id', index: 'id', width: 40, key: true},
            {label: '名称', name: 'name', index: 'name', width: 100},
            {label: '所属部门', name: 'deptName', sortable: false, width: 100},
            {label: '创建时间', name: 'createTime', index: 'create_time', width: 100},
            {
                label: '状态', name: 'status', index: 'status', width: 40, formatter: function (value, options, row) {
                    if (value === 0) {
                        return '<span class="label label-default">新建</span>';
                    } else if (value === 1) {
                        return '<span class="label label-success">发布</span>';
                    } else if (value === 2) {
                        return '<span class="label label-danger">关闭</span>';
                    }
                }
            }
        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });
});

var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "deptId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url: "nourl"
        }
    }
};
var setting1 = {
    data: {
        simpleData: {
            enable: true,
            idKey: "deptId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url: "nourl"
        }
    },
    callback: {
        // 单击事件
        onClick: zTree1OnClick
    }
};

function zTree1OnClick(event, treeId, treeNode) {
    vm.getUser(treeNode.deptId);
}

var ztree;
var ztree1;
var depTreeData;
var userListData;

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        addStep: 1,
        showAdd: false,
        showDetail: false,

        title: null,
        evalTask: {
            status: 1,
            deptId: null,
            deptName: null,

            studentPercentage: null,
            colleaguePercentage: null,
            inspectorPercentage: null,
            otherPercentage: null,

            studentEvalTask: null,
            colleagueEvalTasks: [],
            colleagueEvalTaskItems: [],
            inspectorEvalTasks: [],
            inspectorEvalTaskItems: [],
            otherEvalTask: null
        },
        evalBaseItems: null,
        colleagueEvalBaseItems: null,
        inspectorEvalBaseItems: null,

        subTaskItem: {},
        layer: {
            userList: [],
            chooseUser: {},
            qName: null
        }
    },
    methods: {

        switchList: function () {
            vm.showList = true;
            vm.showAdd = false;
            vm.showDetail = false;
        },
        switchAdd: function () {
            vm.showList = false;
            vm.addStep = 1;
            vm.showAdd = true;
            vm.showDetail = false;
        },
        switchDetail: function () {
            vm.showList = false;
            vm.showAdd = false;
            vm.showDetail = true;
        },
        addStepPre: function () {
            if (vm.addStep > 1) {
                vm.addStep--;
            }
        },
        addStepNext: function () {
            if(vm.addStepDataCheck()){
                vm.addStep++;
            }
        },
        addStepDataCheck: function () {
            if (vm.addStep === 1) {
                if (isBlank(vm.evalTask.name)) {
                    layer.msg("评价任务名称不能为空！", {icon: 5});
                    return false;
                } else if (isBlank(vm.evalTask.deptId)) {
                    layer.msg("评价任务所属部门不能为空！", {icon: 5});
                    return false;
                }
            } else if (vm.addStep === 2) {
                if (isBlank(vm.evalTask.studentPercentage)) {
                    layer.msg("学生评价占比不能为空！", {icon: 5});
                    return false;
                }
            } else if (vm.addStep === 3) {
                let sumPercent = 0;
                vm.evalTask.colleagueEvalTaskItems.forEach( (current) =>{
                    sumPercent += current.percentage;
                } );
                if (isBlank(vm.evalTask.colleaguePercentage)) {
                    layer.msg("同行评价占比不能为空！", {icon: 5});
                    return false;
                } else if (sumPercent !== 100) {
                    layer.msg("同行评价项目占比总和需要为100！", {icon: 5});
                    return false;
                }else if(vm.evalTask.colleagueEvalTasks.filter( value => isBlank(value.userId)).length !== 0){
                    layer.msg("需要为同行评价详情内的每个部门指定评价负责人！", {icon: 5});
                    return false;
                }
            }else if (vm.addStep === 4) {
                let sumPercent = 0;
                vm.evalTask.inspectorEvalTaskItems.forEach( (current) =>{
                    sumPercent += current.percentage;
                } );
                if (isBlank(vm.evalTask.inspectorPercentage)) {
                    layer.msg("督导评价占比不能为空！", {icon: 5});
                    return false;
                } else if (sumPercent !== 100) {
                    layer.msg("督导评价项目占比总和需要为100！", {icon: 5});
                    return false;
                }else if(vm.evalTask.inspectorEvalTasks.length === 0){
                    layer.msg("需要为同行评价详情内的每个部门指定评价负责人！", {icon: 5});
                    return false;
                }
            }else if(vm.addStep === 5){
                let sumPercent = vm.evalTask.studentPercentage + vm.evalTask.colleaguePercentage
                    + vm.evalTask.inspectorPercentage +vm.evalTask.otherPercentage;
                if (isBlank(vm.evalTask.otherPercentage)) {
                    layer.msg("其他评价占比不能为空！", {icon: 5});
                    return false;
                }else if( sumPercent !== 100 ){
                    layer.msg("学生、同行、督导、其他评价占比总和需要为100！", {icon: 5});
                    return false;
                }
            }
            return true;
        },
        query: function () {
            vm.reload();
        },
        getDept: function () {
            //加载部门树
            $.get(baseURL + "sys/dept/list", function (r) {
                ztree = $.fn.zTree.init($("#deptTree"), setting, r);
                //保存部门树
                depTreeData = translateDeptDataToTree(r);
                var node = ztree.getNodeByParam("deptId", vm.evalTask.deptId);
                if (node != null) {
                    ztree.selectNode(node);

                    vm.evalTask.deptName = node.name;
                }
            })
        },
        getEvalBaseItems: function () {
            //加载评价基础标准
            $.get(baseURL + "eval/evalbaseitem/list", function (r) {
                vm.evalBaseItems = r.page.list;
                for (var i = 0; i < vm.evalBaseItems.length; ++i) {
                    if (vm.evalBaseItems[i].name === '学生评价') {
                        vm.evalTask.studentPercentage = vm.evalBaseItems[i].percentage;
                    } else if (vm.evalBaseItems[i].name === '同行评价') {
                        vm.evalTask.colleaguePercentage = vm.evalBaseItems[i].percentage;
                    } else if (vm.evalBaseItems[i].name === '督导评价') {
                        vm.evalTask.inspectorPercentage = vm.evalBaseItems[i].percentage;
                    } else if (vm.evalBaseItems[i].name === '其他评价') {
                        vm.evalTask.otherPercentage = vm.evalBaseItems[i].percentage;
                    }
                }
            })
        },
        getColleagueEvalBaseItems: function () {
            //加载同行评价基础标准
            $.get(baseURL + "eval/colleagueevalbaseitem/list", function (r) {
                vm.colleagueEvalBaseItems = r.page.list;

                for (var i = 0; i < vm.colleagueEvalBaseItems.length; ++i) {
                    vm.evalTask.colleagueEvalTaskItems[i] = {
                        name: vm.colleagueEvalBaseItems[i].name,
                        percentage: vm.colleagueEvalBaseItems[i].percentage,
                        remark: vm.colleagueEvalBaseItems[i].remark
                    };
                }
            })
        },
        getInspectorEvalBaseItems: function () {
            //加载督导评价基础标准
            $.get(baseURL + "eval/inspectorevalbaseitem/list", function (r) {
                vm.inspectorEvalBaseItems = r.page.list;

                for (var i = 0; i < vm.inspectorEvalBaseItems.length; ++i) {
                    vm.evalTask.inspectorEvalTaskItems[i] = {
                        name: vm.inspectorEvalBaseItems[i].name,
                        percentage: vm.inspectorEvalBaseItems[i].percentage,
                        remark: vm.inspectorEvalBaseItems[i].remark
                    };
                }
            })
        },
        detail: function () {
            vm.switchDetail();
        },
        //展示选择用户弹窗
        showSelectUserLayer: function (colleagueEvalTasksIndex, deptId) {
            //清空搜索框
            vm.layer = {
                qName: null,
                chooseUser: {},
                userList: []
            };
            //初始化部门树
            let dept = findDeptFromTreeData(depTreeData, deptId);
            let deptList = translateDeptTreeDataToList(dept);
            ztree1 = $.fn.zTree.init($("#deptTree1"), setting1, deptList);
            //初始化人员
            vm.getUser(deptId);

            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择具体执行人",
                area: ['800px', '410px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#userLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    if (isBlank(vm.layer.chooseUser.userId)) {
                        layer.msg("请从表格中选择一个人员！", {icon: 5});
                    } else {
                        vm.evalTask.colleagueEvalTasks[colleagueEvalTasksIndex].userId = vm.layer.chooseUser.userId;
                        vm.evalTask.colleagueEvalTasks[colleagueEvalTasksIndex].userName = vm.layer.chooseUser.name;
                        Vue.set(vm.evalTask.colleagueEvalTasks);
                        layer.close(index);
                    }
                }
            });
        },
        //弹窗时时更新用户列表
        findUser: function () {
            if (!isBlank(vm.layer.qName)) {
                vm.layer.userList = userListData.filter(value => value.name.indexOf(vm.layer.qName) !== -1);
                Vue.set(vm.layer.userList);
            } else {
                if (userListData.length !== vm.layer.userList.length) {
                    vm.layer.userList = userListData;
                    Vue.set(vm.layer.userList);
                }
            }
        },
        //从后台获取当前部门所有用户数据
        getUser: function (deptId) {
            $.get(baseURL + "sys/user/list/" + deptId, function (r) {
                if (r.code === 0) {
                    //清空搜索
                    vm.layer = {
                        qName: null,
                        chooseUser: {},
                        userList: []
                    };
                    userListData = r.list;
                    vm.layer.userList = userListData;
                    Vue.set(vm.layer.userList);
                } else {
                    console.log(r.msg);
                }
            })
        },
        //弹窗选择用户结果
        layerChooseUser: function (index) {
            vm.layer.chooseUser = vm.layer.userList[index];
            Vue.set(vm.layer.chooseUser);
        },
        addInspectorTask: function () {

        },
        deleteInspectorTask: function () {

        },
        add: function () {
            vm.title = "新增";
            vm.switchAdd();

            vm.evalTask = {
                status: 1,
                deptId: null,
                deptName: null,

                studentPercentage: null,
                colleaguePercentage: null,
                inspectorPercentage: null,
                otherPercentage: null,

                studentEvalTask: null,
                colleagueEvalTasks: [],
                colleagueEvalTaskItems: [],
                inspectorEvalTasks: [],
                inspectorEvalTaskItems: [],
                otherEvalTask: null
            };

            //获取部门
            vm.getDept();
            //获取评价基本标准,同行评价基本标准,督导评价基本标准
            vm.getEvalBaseItems();
            vm.getColleagueEvalBaseItems();
            vm.getInspectorEvalBaseItems();
        },
        update: function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }

            var rowData = $("#jqGrid").getRowData(id);
            if (rowData.status.indexOf('新建') === -1) {
                layer.msg("只有处于新建状态的任务才能修改！", {icon: 2});
                return;
            }

            // vm.showAdd();
            // vm.title = "修改";

            // vm.getInfo(id)
        },
        turnOn: function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }

            var rowData = $("#jqGrid").getRowData(id);
            if (rowData.status.indexOf('新建') === -1) {
                layer.msg("只有处于新建状态的任务才能发布！", {icon: 2});
                return;
            }
        },
        turnOff: function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }

            var rowData = $("#jqGrid").getRowData(id);
            if (rowData.status.indexOf('发布') === -1) {
                layer.msg("只有处于发布状态的任务才能关闭！", {icon: 2});
                return;
            }
        },
        saveOrUpdate: function (event) {

            if(!vm.addStepDataCheck()){
                return false;
            }
            $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function () {
                var url = vm.evalTask.id == null ? "eval/evaltask/save" : "eval/evaltask/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.evalTask),
                    success: function (r) {
                        if (r.code === 0) {
                            layer.msg("操作成功", {icon: 1});
                            vm.reload();
                            $('#btnSaveOrUpdate').button('reset');
                            $('#btnSaveOrUpdate').dequeue();
                        } else {
                            layer.alert(r.msg);
                            $('#btnSaveOrUpdate').button('reset');
                            $('#btnSaveOrUpdate').dequeue();
                        }
                    }
                });
            });
        },
        del: function (event) {
            var ids = getSelectedRows();
            if (ids == null) {
                return;
            }
            var lock = false;
            layer.confirm('确定要删除选中的记录？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                if (!lock) {
                    lock = true;
                    $.ajax({
                        type: "POST",
                        url: baseURL + "eval/evaltask/delete",
                        contentType: "application/json",
                        data: JSON.stringify(ids),
                        success: function (r) {
                            if (r.code == 0) {
                                layer.msg("操作成功", {icon: 1});
                                $("#jqGrid").trigger("reloadGrid");
                            } else {
                                layer.alert(r.msg);
                            }
                        }
                    });
                }
            }, function () {
            });
        },
        getInfo: function (id) {
            $.get(baseURL + "eval/evaltask/info/" + id, function (r) {
                vm.evalTask = r.evalTask;
            });
        },
        //选择部门弹窗
        deptTree: function () {
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择部门",
                area: ['300px', '350px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    //选择上级部门
                    vm.evalTask.deptId = node[0].deptId;
                    vm.evalTask.deptName = node[0].name;
                    //列出子部门，生成同行评价选项
                    if (node[0].children === undefined) {
                        vm.evalTask.colleagueEvalTasks[0] = {};
                        vm.evalTask.colleagueEvalTasks[0].deptId = node[0].deptId;
                        vm.evalTask.colleagueEvalTasks[0].deptName = node[0].name;
                    } else {
                        for (var i = 0; i < node[0].children.length; ++i) {
                            vm.evalTask.colleagueEvalTasks[i] = {};
                            vm.evalTask.colleagueEvalTasks[i].deptId = node[0].children[i].deptId;
                            vm.evalTask.colleagueEvalTasks[i].deptName = node[0].children[i].name;
                        }
                    }
                    layer.close(index);
                }
            });
        },
        //展示子任务项目添加弹窗
        showAddSubTaskItemLayer: function (taskType) {
            vm.subTaskItem = {};
            var subTaskAddTitle;
            if (taskType === 'colleague') {
                subTaskAddTitle = "添加同行评级项目";
            } else if (taskType === 'inspector') {
                subTaskAddTitle = "添加督导评级项目";
            }
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: subTaskAddTitle,
                area: ['600px', '350px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#subTaskItemLayer"),
                btn: ['添加', '取消'],
                btn1: function (index) {
                    if (isBlank(vm.subTaskItem.name)) {
                        layer.msg("名称不能为空！", {icon: 5});
                    } else if (isBlank(vm.subTaskItem.percentage)) {
                        layer.msg("百分比不能为空！", {icon: 5});
                    } else {
                        if (taskType === 'colleague') {
                            Vue.set(vm.evalTask.colleagueEvalTaskItems, vm.evalTask.colleagueEvalTaskItems.length, vm.subTaskItem);
                        } else if (taskType === 'inspector') {
                            Vue.set(vm.evalTask.inspectorEvalTaskItems, vm.evalTask.inspectorEvalTaskItems.length, vm.subTaskItem);
                        }
                        layer.close(index);
                    }
                }
            });
        },
        //删除子任务项目
        deleteSubTaskItem: function (taskType, index) {
            if (taskType === 'colleague') {
                if (vm.evalTask.colleagueEvalTaskItems.length === 1) {
                    layer.msg("最少需要一个同行评价项目！", {icon: 5});
                }
                Vue.delete(vm.evalTask.colleagueEvalTaskItems, index);
            } else if (taskType === 'inspector') {
                if (vm.evalTask.inspectorEvalTaskItems.length === 1) {
                    layer.msg("最少需要一个督导评价项目！", {icon: 5});
                }
                Vue.delete(vm.evalTask.inspectorEvalTaskItems, index);
            }
        },
        reload: function (event) {
            vm.switchList();
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                page: page
            }).trigger("reloadGrid");
        }
    }
});