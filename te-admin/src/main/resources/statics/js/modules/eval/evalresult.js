$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'eval/evalresult/list',
        datatype: "json",
        colModel: [
            {label: '序号', name: 'id', index: 'id', width: 40, key: true, hidden: true},
            {label: '评价任务', name: 'name', index: 'name', width: 100},
            {label: '评价部门', name: 'deptName', sortable: false, width: 100},
            {label: '创建时间', name: 'createTime', index: 'create_time', width: 100}
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


let resultJqGrid =  {
    url: "",
    datatype: "json",
    colModel: [
        { label: 'id', name: 'id', index: 'id', width: 50, key: true,hidden:true },
        { label: '关联评价任务ID', name: 'taskId', index: 'task_id', width: 80,hidden:true },
        { label: '工号', name: 'username',  index: 'username', width: 80 },
        { label: '姓名', name: 'name',  sortable: false, width: 80 },
        { label: '系部', name: 'deptName',  sortable: false, width: 80 },
        { label: '学生评价分数', name: 'studentEvalScore', index: 'student_eval_score', width: 80 },
        { label: '同行评价分数', name: 'colleagueEvalScore', index: 'colleague_eval_score', width: 80 },
        { label: '督导评价分数', name: 'inspectorEvalScore', index: 'inspector_eval_score', width: 80 },
        { label: '其他评价分数', name: 'otherEvalScore', index: 'other_eval_score', width: 80 },
        { label: '总分', name: 'accountScore', index: 'account_score', width: 80 },
        { label: '排名', name: 'ranking', index: 'ranking', width: 40 },
        { label: '评级', name: 'rating', index: 'rating', width: 80 },
        { label: '修改时间', name: 'updateTime', index: 'update_time', width: 80 }
    ],
    viewrecords: true,
    width:800,
    height: 385,
    rowNum: 10,
    rowList : [10,30,50],
    rownumbers: true,
    rownumWidth: 25,
    multiselect: true,
    pager: "#resultListJqGridPager",
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
        $("#resultListJqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
    }
};

let vm = new Vue({
	el:'#rrapp',
	data:{
        q:{
            name: null
        },

		showList: true,
		showResultList: false,
        showResultUpdate:false,

		title: null,
		evalResult: {}
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
        queryResultList: function(event){
            let id = getSelectedRow();
            if (id == null) {
                return;
            }

            $.jgrid.gridUnload("resultListJqGrid");

            let rowData = $("#jqGrid").getRowData(id);
            vm.taskId = rowData.id;

            vm.switchResultList();
            resultJqGrid.url = baseURL + 'eval/evalresult/detail/list/'+id;
            resultJqGrid.width = $("#container_grid")[0].offsetWidth;
            $("#resultListJqGrid").jqGrid(resultJqGrid);
        },
        resultListReload:function(event){
            vm.switchResultList();
            let page = $("#resultListJqGrid").jqGrid('getGridParam','page');
            $("#resultListJqGrid").jqGrid('setGridParam',{
                postData:{'name': vm.q.name},
                page:page
            }).trigger("reloadGrid");
        },
        gotoUpdateResult: function (event) {
			let id = getSelectedRowFromGrid($("#resultListJqGrid"));
			if(id == null){
				return ;
			}
			vm.switchResultUpdate();
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		save: function (event) {
		    $('#btnSave').button('loading').delay(1000).queue(function() {
                $.ajax({
                    type: "POST",
                    url: baseURL + "eval/evalresult/update",
                    contentType: "application/json",
                    data: JSON.stringify(vm.evalResult),
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
		getInfo: function(id){
			$.get(baseURL + "eval/evalresult/info/"+id, function(r){
                vm.evalResult = r.evalResult;
            });
		},
        switchList: function () {
            vm.showList = true;
            vm.showResultList = false;
            vm.showResultUpdate = false;
        },
        switchResultList: function () {
            vm.showList = false;
            vm.showResultList = true;
            vm.showResultUpdate = false;
        },
        switchResultUpdate: function () {
            vm.showList = false;
            vm.showResultList = false;
            vm.showResultUpdate = true;
        }
	}
});