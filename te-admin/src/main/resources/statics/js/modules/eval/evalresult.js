$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'eval/evalresult/list',
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

let vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		evalResult: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		update: function (event) {
			let id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
		    $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function() {
                let url = "eval/evalresult/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.evalResult),
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
		del: function (event) {
            let ids = getSelectedRows();
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
                        url: baseURL + "eval/evalresult/delete",
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
		getInfo: function(id){
			$.get(baseURL + "eval/evalresult/info/"+id, function(r){
                vm.evalResult = r.evalResult;
            });
		},
		reload: function (event) {
			vm.showList = true;
            let page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});