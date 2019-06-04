$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'eval/studentevaltask/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true,hidden:true},
            { label: 'deptId', name: 'deptId', sortable: false, width: 50,hidden:true},
			{ label: '任务名称', name: 'name', sortable: false, width: 80 },
			{ label: '评价部门', name: 'deptName', sortable: false, width: 80 },
			{ label: '创建时间', name: 'createTime', sortable: false, width: 80 }
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

let recordJqGrid =  {
    url: "",
    datatype: "json",
    colModel: [
        { label: 'id', name: 'id', index: 'id', width: 50, key: true,hidden:true},
        { label: '姓名', name: 'userName', sortable: false, width: 80 },
        { label: '分数', name: 'score', index:'score', width: 80 },
        { label: '创建时间', name: 'updateTime', index:'update_time',  width: 80 }
    ],
    viewrecords: true,
    width:800,
    height: 385,
    rowNum: 10,
    rowList : [10,30,50],
    rownumbers: true,
    rownumWidth: 25,
    multiselect: true,
    pager: "#recordListJqGridPager",
    jsonReader : {
        root: "page.list",
        page: "page.currPage",
        total: "page.totalPage",
        records: "page.totalCount"
    },
    prmNames : {
        page:"page",
        rows:"limit",
        order: "order"
    },
    gridComplete:function(){
        //隐藏grid底部滚动条
        $("#recordListJqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
    }
};

//保存获取的部门列表(树状结构数据)
let depTreeData;
//保存获取的用户列表
let userListData;

let vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
        showRecordList: false,
        showRecordAdd:false,
        showRecordImport:false,
        showRecordImportResult:false,
        showRecordUpdate:false,

		title: null,

        subTaskId:null,
        evalTaskDeptId:null,

        studentEvalTask: {},
        studentEvalRecord:{},

        importRecordSuccessList:{},
        importRecordErrorList:{},

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
		reload: function (event) {
			vm.switchList();
			let page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		},
        queryRecordList:function() {
            let id = getSelectedRow();
            if (id == null) {
                return;
            }

            let rowData = $("#jqGrid").getRowData(id);
            vm.title = '评价任务 >> '+rowData.name;
            vm.subTaskId = rowData.id;
            vm.evalTaskDeptId = rowData.deptId;

            vm.switchTaskList();
            recordJqGrid.url = baseURL + 'eval/studentevalrecord/list/'+id;
            recordJqGrid.width = $("#container_grid")[0].offsetWidth;
            $("#recordListJqGrid").jqGrid(recordJqGrid);
        },
        recordListReload: function (event) {
            vm.switchTaskList();
            let page = $("#recordListJqGrid").jqGrid('getGridParam','page');
            $("#recordListJqGrid").jqGrid('setGridParam',{
                page:page
            }).trigger("reloadGrid");
        },
        gotoAddRecord: function(){

		    vm.getDept();

            vm.switchRecordAdd();
            vm.title = "新增";
            vm.studentEvalRecord = {};
            vm.studentEvalRecord.subTaskId = vm.subTaskId;
        },
        gotoImportRecord:function(){
            //学生评价批量导入记录
            let uploadUrl = baseURL + "eval/studentevalrecord/import/"+vm.subTaskId;
            $("#xlsRecordFile").fileinput({
                language: 'zh', //设置语言
                uploadUrl: uploadUrl, //上传的地址
                enctype : 'multipart/form-data',
                maxFileCount:1,//只允许上传一个文件
                autoReplace: true,//是否自动替换
                uploadAsync: true,//异步上传
                showRemove : false,//是否显示移除按钮
                showCancel: false,//是否显示取消按钮
                showClose: false,//是否显示关闭按钮
                allowedFileExtensions: ['xls', 'xlsx'],//支持的文件拓展名
                errorCloseButton: ''//隐藏错误提示关闭按钮
            });
            $("#xlsRecordFile").on("fileuploaded", function(event, data, previewId, index) {
                console.log(data.response);
               vm.importRecordSuccessList=data.response.successList;
               vm.importRecordErrorList=data.response.errorList;
               Vue.set(vm.importRecordSuccessList);
               Vue.set(vm.importRecordErrorList);
                vm.showRecordImportResult = true;
            });

            vm.switchRecordImport();
            vm.title = "批量导入";

        },
        gotoUpdateRecord: function (event) {
            let id = getRecordListSelectedRow();
            if(id == null){
                return ;
            }
            vm.switchRecordUpdate();
            vm.title = "修改";

            vm.getRecordInfo(id)
        },
        saveRecord: function (event) {
            $('#btnSave').button('loading').delay(1000).queue(function() {
                $.ajax({
                    type: "POST",
                    url: baseURL + "eval/studentevalrecord/save",
                    contentType: "application/json",
                    data: JSON.stringify(vm.studentEvalRecord),
                    success: function(r){
                        if(r.code === 0){
                            layer.msg("操作成功", {icon: 1});
                            vm.reload();
                            $('#btnSave').button('reset');
                            $('#btnSave').dequeue();
                        }else{
                            layer.alert(r.msg);
                            $('#btnSave').button('reset');
                            $('#btnSave').dequeue();
                        }
                    }
                });
            });
        },
        updateRecord: function (event) {
            $('#btnUpdate').button('loading').delay(1000).queue(function() {
                $.ajax({
                    type: "POST",
                    url: baseURL + "eval/studentevalrecord/update",
                    contentType: "application/json",
                    data: JSON.stringify(vm.studentEvalRecord),
                    success: function(r){
                        if(r.code === 0){
                            layer.msg("操作成功", {icon: 1});
                            vm.reload();
                            $('#btnUpdate').button('reset');
                            $('#btnUpdate').dequeue();
                        }else{
                            layer.alert(r.msg);
                            $('#btnUpdate').button('reset');
                            $('#btnUpdate').dequeue();
                        }
                    }
                });
            });
        },
        importRecord:  function (event) {
            $('#btnImport').button('loading').delay(1000).queue(function() {
                $('#btnImport').button('reset');
                $('#btnImport').dequeue();
            });
        },
        delRecord: function (event) {
            let ids = getRecordListSelectedRows();
            if(ids == null){
                return ;
            }
            let lock = false;
            layer.confirm('确定要删除选中的记录？', {
                btn: ['确定','取消'] //按钮
            }, function(){
                if(!lock) {
                    lock = true;
                    $.ajax({
                        type: "POST",
                        url: baseURL + "eval/studentevalrecord/delete",
                        contentType: "application/json",
                        data: JSON.stringify(ids),
                        success: function(r){
                            if(r.code == 0){
                                layer.msg("操作成功", {icon: 1});
                                $("#jqGrid").trigger("reloadGrid");
                            }else{
                                layer.alert(r.msg);
                            }
                        }
                    });
                }
            }, function(){
            });
        },
        getRecordInfo: function(id){
            $.get(baseURL + "eval/studentevalrecord/info/"+id, function(r){
                vm.studentEvalRecord = r.studentEvalRecord;
            });
        },
        //从后台获取当前部门部门数据
        getDept: function () {
            //获取并保存部门数据
            getDeptTreeData(function (deptList) {
                depTreeData = translateDeptDataToTree(deptList);
            });
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
        //弹窗选择用户结果
        layerChooseUser: function (index) {
            vm.layer.chooseUser = vm.layer.userList[index];
            Vue.set(vm.layer.chooseUser);
        },
        addUser:function(){
            //获取部门数据
            let deptData = findDeptFromTreeData(depTreeData, vm.evalTaskDeptId);
            //显示选择用户弹窗
            showSelectUserLayer($("#deptTree1"), deptData,  vm.evalTaskDeptId, "选择用户"
                , jQuery("#userLayer"), vm.getUser, function (index) {
                    if (isBlank(vm.layer.chooseUser.userId)) {
                        layer.msg("请从表格中选择一个人员！", {icon: 5});
                    } else {
                        vm.studentEvalRecord.userId = vm.layer.chooseUser.userId;
                        vm.studentEvalRecord.userName = vm.layer.chooseUser.name;
                        layer.close(index);
                    }
                });
        },
        switchList: function () {
            vm.showList = true;
            vm.showRecordList = false;
            vm.showRecordAdd = false;
            vm.showRecordImport = false;
            vm.showRecordImportResult = false;
            vm.showRecordUpdate = false;
        },
        switchTaskList: function () {
            vm.showList = false;
            vm.showRecordList = true;
            vm.showRecordAdd = false;
            vm.showRecordImport = false;
            vm.showRecordImportResult = false;
            vm.showRecordUpdate = false;
        },
        switchRecordAdd: function () {
            vm.showList = false;
            vm.showRecordList = false;
            vm.showRecordAdd = true;
            vm.showRecordImport = false;
            vm.showRecordImportResult = false;
            vm.showRecordUpdate = false;
        },
        switchRecordImport: function () {
            vm.showList = false;
            vm.showRecordList = false;
            vm.showRecordAdd = false;
            vm.showRecordImport = true;
            vm.showRecordImportResult = false;
            vm.showRecordUpdate = false;
        },
        switchRecordUpdate: function () {
            vm.showList = false;
            vm.showRecordList = false;
            vm.showRecordAdd = false;
            vm.showRecordImport = false;
            vm.showRecordImportResult = false;
            vm.showRecordUpdate = true;
        }
	}
});