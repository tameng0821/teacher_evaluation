$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'eval/evaltask/list',
        datatype: "json",
        colModel: [
            {label: '序号', name: 'id', index: 'id', width: 40, key: true, hidden: true},
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
                    } else if (value === 3) {
                        return '<span class="label label-info">完成</span>';
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

//添加部门时部门ztree
let ztree;
//保存获取的部门列表(树状结构数据)
let depTreeData;
//保存获取的用户列表
let userListData;

let vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        addStep: 1,
        showAdd: false,
        showDetail: false,

        title: null,
        evalTask: {
            status: 0,
            deptId: null,
            deptName: null,

            studentPercentage: null,
            colleaguePercentage: null,
            inspectorPercentage: null,
            otherPercentage: null,

            studentEvalTask: {},
            colleagueEvalTasks: [],
            colleagueEvalTaskItems: [],
            inspectorEvalTasks: [],
            inspectorEvalTaskItems: [],
            otherEvalTask: {}
        },
        //弹窗添加同行/督导评价子项目存储使用
        subTaskItem: {},
        //弹窗添加人员时使用
        layer: {
            userList: [],
            chooseUser: {},
            qName: null
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        getDept: function () {
            //获取并保存部门数据
            getDeptTreeData(function (deptList) {
                depTreeData = translateDeptDataToTree(deptList);
                ztree = initDeptSelectDeptTree($("#deptTree"), depTreeData);
            });
        },
        getEvalBaseItems: function () {
            //加载评价基础标准
            $.get(baseURL + "eval/evalbaseitem/list", function (r) {
                for (let i = 0; i < r.page.list.length; ++i) {
                    if (r.page.list[i].name === '学生评价') {
                        vm.evalTask.studentPercentage = r.page.list[i].percentage;
                    } else if (r.page.list[i].name === '同行评价') {
                        vm.evalTask.colleaguePercentage = r.page.list[i].percentage;
                    } else if (r.page.list[i].name === '督导评价') {
                        vm.evalTask.inspectorPercentage = r.page.list[i].percentage;
                    } else if (r.page.list[i].name === '其他评价') {
                        vm.evalTask.otherPercentage = r.page.list[i].percentage;
                    }
                }
            })
        },
        getColleagueEvalBaseItems: function () {
            //加载同行评价基础标准
            $.get(baseURL + "eval/colleagueevalbaseitem/list", function (r) {
                for (let i = 0; i < r.page.list.length; ++i) {
                    vm.evalTask.colleagueEvalTaskItems[i] = {
                        name: r.page.list[i].name,
                        percentage: r.page.list[i].percentage,
                        remark: r.page.list[i].remark
                    };
                }
            })
        },
        getInspectorEvalBaseItems: function () {
            //加载督导评价基础标准
            $.get(baseURL + "eval/inspectorevalbaseitem/list", function (r) {
                for (let i = 0; i < r.page.list.length; ++i) {
                    vm.evalTask.inspectorEvalTaskItems[i] = {
                        name: r.page.list[i].name,
                        percentage: r.page.list[i].percentage,
                        remark: r.page.list[i].remark
                    };
                }
            })
        },

        detail: function () {
            let id = getSelectedRow();
            if (id == null) {
                return;
            }

            vm.switchDetail();
            vm.title = "查看详情";

            vm.getInfo(id);
        },
        //选择添加系主任/同行进行评价
        addColleagueTask: function (colleagueEvalTasksIndex, deptId) {
            //获取部门数据
            let deptData = findDeptFromTreeData(depTreeData, deptId);
            //显示选择用户弹窗
            showSelectUserLayer($("#deptTree1"), deptData, deptId, "选择系主任/同事完成同行评价"
                , jQuery("#userLayer"), vm.getUser, function (index) {
                    if (isBlank(vm.layer.chooseUser.userId)) {
                        layer.msg("请从表格中选择一个人员！", {icon: 5});
                    } else {
                        vm.evalTask.colleagueEvalTasks[colleagueEvalTasksIndex].userId = vm.layer.chooseUser.userId;
                        vm.evalTask.colleagueEvalTasks[colleagueEvalTasksIndex].userName = vm.layer.chooseUser.name;
                        Vue.set(vm.evalTask.colleagueEvalTasks, colleagueEvalTasksIndex, vm.evalTask.colleagueEvalTasks[colleagueEvalTasksIndex]);
                        layer.close(index);
                    }
                });
        },
        //弹窗时时更新用户列表
        findUser: function () {
            if (!isBlank(vm.layer.qName)) {
                vm.layer.userList = userListData.filter(value => value.name.indexOf(vm.layer.qName.trim()) !== -1);
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
            //清空搜索
            vm.layer = {
                qName: null,
                chooseUser: {},
                userList: []
            };
            //从后台获取用户列表
            getUserListData(deptId,function (userList) {
                userListData = userList;
                vm.layer.userList = userListData;
                Vue.set(vm.layer.userList);
            });
        },
        //弹窗选择用户结果
        layerChooseUser: function (index) {
            vm.layer.chooseUser = vm.layer.userList[index];
            Vue.set(vm.layer.chooseUser);
        },
        addInspectorTask: function () {
            //获取部门数据
            let deptData = findDeptFromTreeData(depTreeData, vm.evalTask.deptId);
            //显示选择用户弹窗
            showSelectUserLayer($("#deptTree1"), deptData, vm.evalTask.deptId, "选择督导评价"
                , jQuery("#userLayer"), vm.getUser, function (index) {
                    if (isBlank(vm.layer.chooseUser.userId)) {
                        layer.msg("请从表格中选择一个人员！", {icon: 5});
                    } else if (vm.evalTask.inspectorEvalTasks.filter(value => value.userId === vm.layer.chooseUser.userId).length > 0) {
                        layer.msg("切勿选择重复的人员！", {icon: 5});
                    } else {
                        let inspectorEvalTask = {
                            userId: vm.layer.chooseUser.userId,
                            userName: vm.layer.chooseUser.name
                        };
                        Vue.set(vm.evalTask.inspectorEvalTasks, vm.evalTask.inspectorEvalTasks.length, inspectorEvalTask);
                        layer.close(index);
                    }
                });
        },
        deleteInspectorTask: function (index) {
            if (vm.evalTask.inspectorEvalTasks.length === 1) {
                layer.msg("最少需要一个同行评价项目！", {icon: 5});
            }
            Vue.delete(vm.evalTask.inspectorEvalTasks, index);
        },
        add: function () {
            vm.title = "新增";
            vm.switchAdd();

            vm.evalTask = {
                status: 0,
                deptId: null,
                deptName: null,

                studentPercentage: null,
                colleaguePercentage: null,
                inspectorPercentage: null,
                otherPercentage: null,

                studentEvalTask: {},
                colleagueEvalTasks: [],
                colleagueEvalTaskItems: [],
                inspectorEvalTasks: [],
                inspectorEvalTaskItems: [],
                otherEvalTask: {}
            };

            //获取部门
            vm.getDept();
            //获取评价基本标准,同行评价基本标准,督导评价基本标准
            vm.getEvalBaseItems();
            vm.getColleagueEvalBaseItems();
            vm.getInspectorEvalBaseItems();
        },
        update: function (event) {
            let id = getSelectedRow();
            if (id == null) {
                return;
            }

            let rowData = $("#jqGrid").getRowData(id);
            if (rowData.status.indexOf('新建') === -1) {
                layer.msg("只有处于新建状态的任务才能修改！", {icon: 2});
                return;
            }

            //获取部门
            vm.getDept();

            vm.switchAdd();
            vm.title = "修改";

            vm.getInfo(id);
        },
        release: function (event) {
            let id = getSelectedRow();
            if (id == null) {
                return;
            }

            let rowData = $("#jqGrid").getRowData(id);
            if( !(rowData.status.indexOf('新建') !== -1 || rowData.status.indexOf('关闭') !== -1 ) ) {
                layer.msg("只有处于新建或者状态的任务才能发布！", {icon: 2});
                return;
            }

            let lock = false;
            layer.confirm('评价任务将公开给评价人使用，确定要发布？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                if (!lock) {
                    lock = true;
                    $.get(baseURL + "eval/evaltask/release/" + id, function (r) {
                        if (r.code === 0) {
                            layer.msg("评价任务发布成功", {icon: 1});
                            vm.reload();
                        } else {
                            layer.alert(r.msg);
                        }
                    });
                }
            }, function () {
            });
        },
        close: function (event) {
            let id = getSelectedRow();
            if (id == null) {
                return;
            }

            let rowData = $("#jqGrid").getRowData(id);
            if (rowData.status.indexOf('发布') === -1) {
                layer.msg("只有处于发布状态的任务才能关闭！", {icon: 2});
                return;
            }

            let lock = false;
            layer.confirm('评价任务将不再发布，即其他人将不再能评价，确定要关闭？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                if (!lock) {
                    lock = true;
                    $.get(baseURL + "eval/evaltask/close/" + id, function (r) {
                        if (r.code === 0) {
                            layer.msg("评价任务关闭成功", {icon: 1});
                            vm.reload();
                        } else {
                            layer.alert(r.msg);
                        }
                    });
                }
            }, function () {
            });
        },
        generateResult: function (event) {
            let id = getSelectedRow();
            if (id == null) {
                return;
            }

            let rowData = $("#jqGrid").getRowData(id);
            if (rowData.status.indexOf('关闭') === -1) {
                layer.msg("只有处于关闭状态的任务才能生成评价结果！", {icon: 2});
                return;
            }

            let lock = false;
            layer.confirm('评价任务将生成评价结果，确定？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                if (!lock) {
                    $.get(baseURL + "eval/evaltask/result/" + id, function (r) {
                        lock = true;
                        if (r.code === 0) {
                            layer.msg("评价结果生成完成，请去评价结果模块查看!", {icon: 1});
                            vm.reload();
                        } else {
                            layer.alert(r.msg);
                        }
                    });
                }
            }, function () {
            });
        },
        saveOrUpdate: function (event) {
            if (!vm.addStepDataCheck()) {
                return false;
            }
            $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function () {
                let url = vm.evalTask.id == null ? "eval/evaltask/save" : "eval/evaltask/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.evalTask),
                    success: function (r) {
                        if (r.code === 0) {
                            layer.msg("操作成功", {icon: 1});
                            vm.reload();
                        } else {
                            layer.alert(r.msg);
                        }
                        $('#btnSaveOrUpdate').button('reset');
                        $('#btnSaveOrUpdate').dequeue();
                    }
                });
            });
        },
        del: function (event) {
            let ids = getSelectedRows();
            if (ids == null) {
                return;
            }
            let lock = false;
            layer.confirm('评价记录将会级联删除，确定要删除选中的记录？', {
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
                            if (r.code === 0) {
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
                    let node = ztree.getSelectedNodes();
                    //选择上级部门
                    vm.evalTask.deptId = node[0].deptId;
                    vm.evalTask.deptName = node[0].name;
                    //列出子部门，生成同行评价详情
                    vm.evalTask.colleagueEvalTasks = [];
                    if (node[0].children === undefined) {
                        vm.evalTask.colleagueEvalTasks[0] = {};
                        vm.evalTask.colleagueEvalTasks[0].deptId = node[0].deptId;
                        vm.evalTask.colleagueEvalTasks[0].deptName = node[0].name;
                    } else {
                        for (let i = 0; i < node[0].children.length; ++i) {
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
            let subTaskAddTitle;
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
        },
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
            if (vm.addStepDataCheck()) {
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
                vm.evalTask.colleagueEvalTaskItems.forEach((current) => {
                    if (current.percentage) {
                        sumPercent += parseInt(current.percentage);
                    }
                });
                if (isBlank(vm.evalTask.colleaguePercentage)) {
                    layer.msg("同行评价占比不能为空！", {icon: 5});
                    return false;
                } else if (vm.evalTask.colleagueEvalTaskItems.filter(value => isBlank(value.name)).length !== 0) {
                    layer.msg("同行评价项目名称不能为空！", {icon: 5});
                    return false;
                } else if (sumPercent !== 100) {
                    layer.msg("同行评价项目占比总和需要为100！", {icon: 5});
                    return false;
                } else if (vm.evalTask.colleagueEvalTasks.filter(value => isBlank(value.userId)).length !== 0) {
                    layer.msg("需要为同行评价详情内的每个部门指定评价负责人！", {icon: 5});
                    return false;
                }
            } else if (vm.addStep === 4) {
                let sumPercent = 0;
                vm.evalTask.inspectorEvalTaskItems.forEach((current) => {
                    if (current.percentage) {
                        sumPercent += parseInt(current.percentage);
                    }
                });
                if (isBlank(vm.evalTask.inspectorPercentage)) {
                    layer.msg("督导评价占比不能为空！", {icon: 5});
                    return false;
                } else if (vm.evalTask.inspectorEvalTaskItems.filter(value => isBlank(value.name)).length !== 0) {
                    layer.msg("同行评价项目名称不能为空！", {icon: 5});
                    return false;
                } else if (sumPercent !== 100) {
                    layer.msg("督导评价项目占比总和需要为100！", {icon: 5});
                    return false;
                } else if (vm.evalTask.inspectorEvalTasks.length === 0) {
                    layer.msg("需要为督导评价详情指定至少一个评价负责人！", {icon: 5});
                    return false;
                }
            } else if (vm.addStep === 5) {
                let sumPercent = 0;
                if (vm.evalTask.studentPercentage) {
                    sumPercent += parseInt(vm.evalTask.studentPercentage);
                }
                if (vm.evalTask.colleaguePercentage) {
                    sumPercent += parseInt(vm.evalTask.colleaguePercentage);
                }
                if (vm.evalTask.inspectorPercentage) {
                    sumPercent += parseInt(vm.evalTask.inspectorPercentage);
                }
                if (vm.evalTask.otherPercentage) {
                    sumPercent += parseInt(vm.evalTask.otherPercentage);
                }
                if (isBlank(vm.evalTask.otherPercentage)) {
                    layer.msg("其他评价占比不能为空！", {icon: 5});
                    return false;
                } else if (sumPercent !== 100) {
                    layer.msg("学生、同行、督导、其他评价占比总和需要为100！", {icon: 5});
                    return false;
                }
            }
            return true;
        }
    }
});