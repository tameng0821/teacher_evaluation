$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'eval/studentevaltask/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true,hidden:true},
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

let vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
        showRecordList: false,
        showRecordAdd:false,
        showRecordImport:false,
        showRecordUpdate:false,

		title: null,
		studentEvalTask: {},
        studentEvalRecord:{}
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
        addRecord: function(){
            vm.switchRecordAdd();
            vm.title = "新增";
            vm.studentEvalRecord = {};
        },
        importRecord:function(){
            vm.switchRecordImport();
            vm.title = "批量导入";
        },
        updateRecord: function (event) {
            let id = getRecordListSelectedRow();
            if(id == null){
                return ;
            }
            vm.switchRecordUpdate();
            vm.title = "修改";

            vm.getRecordInfo(id)
        },
        saveOrUpdateRecord: function (event) {
            $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function() {
                let url = vm.studentEvalRecord.id == null ? "eval/studentevalrecord/save" : "eval/studentevalrecord/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.studentEvalRecord),
                    success: function(r){
                        if(r.code === 0){
                            layer.msg("操作成功", {icon: 1});
                            vm.reload();
                            $('#btnSaveOrUpdate').button('reset');
                            $('#btnSaveOrUpdate').dequeue();
                        }else{
                            layer.alert(r.msg);
                            $('#btnSaveOrUpdate').button('reset');
                            $('#btnSaveOrUpdate').dequeue();
                        }
                    }
                });
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
        switchList: function () {
            vm.showList = true;
            vm.showRecordList = false;
            vm.showRecordAdd = false;
            vm.showRecordImport = false;
            vm.showRecordUpdate = false;
        },
        switchTaskList: function () {
            vm.showList = false;
            vm.showRecordList = true;
            vm.showRecordAdd = false;
            vm.showRecordImport = false;
            vm.showRecordUpdate = false;
        },
        switchRecordAdd: function () {
            vm.showList = false;
            vm.showRecordList = false;
            vm.showRecordAdd = true;
            vm.showRecordImport = false;
            vm.showRecordUpdate = false;
        },
        switchRecordImport: function () {
            vm.showList = false;
            vm.showRecordList = false;
            vm.showRecordAdd = false;
            vm.showRecordImport = true;
            vm.showRecordUpdate = false;
        },
        switchRecordUpdate: function () {
            vm.showList = false;
            vm.showRecordList = false;
            vm.showRecordAdd = false;
            vm.showRecordImport = false;
            vm.showRecordUpdate = true;
        }
	}
});