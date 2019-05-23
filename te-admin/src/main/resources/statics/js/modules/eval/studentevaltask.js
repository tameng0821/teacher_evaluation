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

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		studentEvalTask: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
        selectTask:function() {
            // let id = getSelectedRow();
            // if (id == null) {
            //     return;
            // }
		    contentFrameLoad('main.html');
        },
		getInfo: function(id){
			$.get(baseURL + "eval/studentevaltask/info/"+id, function(r){
                vm.studentEvalTask = r.studentEvalTask;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});