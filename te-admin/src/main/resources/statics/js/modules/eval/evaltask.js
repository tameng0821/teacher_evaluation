$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'eval/evaltask/list',
        datatype: "json",
        colModel: [			
			{ label: '序号', name: 'id', index: 'id', width: 40, key: true },
			{ label: '名称', name: 'name', index: 'name', width: 100 },
            { label: '所属部门', name: 'deptName', sortable: false, width: 100 },
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 100 },
			{ label: '状态', name: 'status', index: 'status', width: 40 , formatter: function(value, options, row){
			        if(value === 0){
			            return '<span class="label label-default">新建</span>';
                    }else if(value === 1){
                        return '<span class="label label-success">发布</span>';
                    }else if(value === 2){
                        return '<span class="label label-danger">关闭</span>';
                    }
			}}
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

var setting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "deptId",
            pIdKey: "parentId",
            rootPId: -1
        },
        key: {
            url:"nourl"
        }
    }
};
var ztree;

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
        addStep:1,
        showAdd: false,
		showDetail: false,

		title: null,
		evalTask: {
            status:1,
            deptId:null,
            deptName:null,

            studentPercentage:null,
            colleaguePercentage:null,
            inspectorPercentage:null,
            otherPercentage:null,

            studentEvalTask:null,
            colleagueEvalTasks:null,
            colleagueEvalTaskItems:null,
            inspectorEvalTasks:null,
            inspectorEvalTaskItems:null,
            otherEvalTask:null
        },
        evalBaseItems:null,
        colleagueEvalBaseItems:null,
        inspectorEvalBaseItems:null,

        subTaskItem: {}
	},
	methods: {

	    switchList:function(){
	        vm.showList = true;
	        vm.showAdd = false;
	        vm.showDetail = false;
        },
        switchAdd:function(){
            vm.showList = false;
            vm.addStep = 1;
            vm.showAdd = true;
            vm.showDetail = false;
        },
        switchDetail:function(){
            vm.showList = false;
            vm.showAdd = false;
            vm.showDetail = true;
        },
        addStepPre:function(){
            if(vm.addStep > 1){
                vm.addStep--;
            }
        },
        addStepNext:function(){
            vm.addStep++;
        },
		query: function () {
			vm.reload();
		},
        getDept: function(){
            //加载部门树
            $.get(baseURL + "sys/dept/list", function(r){
                ztree = $.fn.zTree.init($("#deptTree"), setting, r);
                var node = ztree.getNodeByParam("deptId", vm.evalTask.deptId);
                if(node != null){
                    ztree.selectNode(node);

                    vm.evalTask.deptName = node.name;
                }
            })
        },
        getEvalBaseItems: function() {
            //加载评价基础标准
            $.get(baseURL + "eval/evalbaseitem/list", function (r) {
                vm.evalBaseItems = r.page.list;
                for (var i=0;i < vm.evalBaseItems.length;++i) {
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
        getColleagueEvalBaseItems: function(){
            //加载同行评价基础标准
            $.get(baseURL + "eval/colleagueevalbaseitem/list", function(r){
                vm.colleagueEvalBaseItems = r.page.list;

                for(var i=0;i < vm.colleagueEvalBaseItems.length;++i){
                    vm.evalTask.colleagueEvalTaskItems[i] = {
                        name:vm.colleagueEvalBaseItems[i].name,
                        percentage:vm.colleagueEvalBaseItems[i].percentage,
                        remark:vm.colleagueEvalBaseItems[i].remark
                    };
                }
            })
        },
        getInspectorEvalBaseItems: function(){
            //加载督导评价基础标准
            $.get(baseURL + "eval/inspectorevalbaseitem/list", function(r){
                vm.inspectorEvalBaseItems = r.page.list;

                for(var i=0;i < vm.inspectorEvalBaseItems.length;++i){
                    vm.evalTask.inspectorEvalTaskItems[i] = {
                        name:vm.inspectorEvalBaseItems[i].name,
                        percentage:vm.inspectorEvalBaseItems[i].percentage,
                        remark:vm.inspectorEvalBaseItems[i].remark
                    };
                }
            })
        },
        detail:function(){
            vm.switchDetail();
        },
        addSubTask:function(taskType){
            vm.subTaskItem = {};
	        var subTaskAddTitle;
	        if(taskType === 'colleague'){
                subTaskAddTitle = "添加同行评级项目";
            }else if(taskType === 'inspector'){
                subTaskAddTitle = "添加督导评级项目";
            }
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: subTaskAddTitle,
                area: ['600px', '420px'],
                shade: 0,
                shadeClose: false,
                content: jQuery("#subTaskItemLayer"),
                btn: ['添加', '取消'],
                btn1: function (index) {
                    if(isBlank(vm.subTaskItem.name)){
                        layer.msg("名称不能为空！", {icon: 5});
                    }else if(isBlank(vm.subTaskItem.percentage)){
                        layer.msg("百分比不能为空！", {icon: 5});
                    }else {
                        if(taskType === 'colleague'){
                            Vue.set(vm.evalTask.colleagueEvalTaskItems,vm.evalTask.colleagueEvalTaskItems.length,vm.subTaskItem);
                        }else if(taskType === 'inspector'){
                            Vue.set(vm.evalTask.inspectorEvalTaskItems,vm.evalTask.inspectorEvalTaskItems.length,vm.subTaskItem);
                        }
                        layer.close(index);
                    }
                }
            });
        },
        deleteSubTaskItem:function(taskType,index){
            if(taskType === 'colleague'){
                if(vm.evalTask.colleagueEvalTaskItems.length === 1){
                    layer.msg("最少需要一个同行评价项目！", {icon: 5});
                }
                Vue.delete(vm.evalTask.colleagueEvalTaskItems,index);
            }else if(taskType === 'inspector'){
                if(vm.evalTask.inspectorEvalTaskItems.length === 1){
                    layer.msg("最少需要一个督导评价项目！", {icon: 5});
                }
                Vue.delete(vm.evalTask.inspectorEvalTaskItems,index);
            }
        },
        addInspectorTask:function(){

        },
        deleteInspectorTask:function(){

        },
		add: function(){
            vm.title = "新增";
			vm.switchAdd();

            vm.evalTask = {
                status:1,
                deptId:null,
                deptName:null,

                studentPercentage:null,
                colleaguePercentage:null,
                inspectorPercentage:null,
                otherPercentage:null,

                studentEvalTask:null,
                colleagueEvalTasks:[],
                colleagueEvalTaskItems:[],
                inspectorEvalTasks:[],
                inspectorEvalTaskItems:[],
                otherEvalTask:null
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
			if(id == null){
				return ;
			}

            var rowData = $("#jqGrid").getRowData(id);
            if(rowData.status.indexOf('新建') === -1){
                layer.msg("只有处于新建状态的任务才能修改！", {icon: 2});
                return;
            }

			// vm.showAdd();
            // vm.title = "修改";
            
            // vm.getInfo(id)
		},
        turnOn: function (event) {
            var id = getSelectedRow();
            if(id == null){
                return ;
            }

            var rowData = $("#jqGrid").getRowData(id);
            if(rowData.status.indexOf('新建') === -1){
                layer.msg("只有处于新建状态的任务才能发布！", {icon: 2});
                return;
            }
        },
        turnOff: function (event) {
            var id = getSelectedRow();
            if(id == null){
                return ;
            }

            var rowData = $("#jqGrid").getRowData(id);
            if(rowData.status.indexOf('发布') === -1){
                layer.msg("只有处于发布状态的任务才能关闭！", {icon: 2});
                return;
            }
        },
		saveOrUpdate: function (event) {
		    $('#btnSaveOrUpdate').button('loading').delay(1000).queue(function() {
                var url = vm.evalTask.id == null ? "eval/evaltask/save" : "eval/evaltask/update";
                $.ajax({
                    type: "POST",
                    url: baseURL + url,
                    contentType: "application/json",
                    data: JSON.stringify(vm.evalTask),
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
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			var lock = false;
            layer.confirm('确定要删除选中的记录？', {
                btn: ['确定','取消'] //按钮
            }, function(){
               if(!lock) {
                    lock = true;
		            $.ajax({
                        type: "POST",
                        url: baseURL + "eval/evaltask/delete",
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
			$.get(baseURL + "eval/evaltask/info/"+id, function(r){
                vm.evalTask = r.evalTask;
            });
		},
        deptTree: function(){
            layer.open({
                type: 1,
                offset: '50px',
                skin: 'layui-layer-molv',
                title: "选择部门",
                area: ['300px', '420px'],
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
                    if(node[0].children === undefined){
                        vm.evalTask.colleagueEvalTasks[0] = {};
                        vm.evalTask.colleagueEvalTasks[0].deptId = node[0].deptId;
                        vm.evalTask.colleagueEvalTasks[0].deptName = node[0].name;
                    }else {
                        for(var i=0; i < node[0].children.length ; ++i){
                            vm.evalTask.colleagueEvalTasks[i] = {};
                            vm.evalTask.colleagueEvalTasks[i].deptId = node[0].children[i].deptId;
                            vm.evalTask.colleagueEvalTasks[i].deptName = node[0].children[i].name;
                        }
                    }
                    layer.close(index);
                }
            });
        },
		reload: function (event) {
			vm.switchList();
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});