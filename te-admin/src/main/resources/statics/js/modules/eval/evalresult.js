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
        { label: '学生评价', name: 'studentEvalScore', index: 'student_eval_score', width: 50 },
        { label: '同行评价', name: 'colleagueEvalScore', index: 'colleague_eval_score', width: 50 },
        { label: '督导评价', name: 'inspectorEvalScore', index: 'inspector_eval_score', width: 50 },
        { label: '其他评价', name: 'otherEvalScore', index: 'other_eval_score', width: 50 },
        { label: '总分', name: 'accountScore', index: 'account_score', width: 40 },
        { label: '排名', name: 'ranking', index: 'ranking', width: 40 },
        { label: '评级', name: 'rating', index: 'rating', width: 40 },
        { label: '生成时间', name: 'updateTime', index: 'update_time', width: 100 }
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
            name: null,
            ranking1:null,
            ranking2:null,
            selectedDept: null,
            deptList: [],
            selectedRating: null,
            rakingList:['优秀', '良好', '合格' , '不合格']
        },

		showList: true,
		showResultList: false,
        showSummary: false,
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

            //获取部门列表
            $.get(baseURL + 'eval/evalresult/detail/deptList/'+id, function(r){
                vm.q.deptList = r.deptList;
            });

            vm.switchResultList();
            resultJqGrid.url = baseURL + 'eval/evalresult/detail/list/'+id;
            resultJqGrid.width = $("#container_grid")[0].offsetWidth;
            $("#resultListJqGrid").jqGrid(resultJqGrid);
        },
        resultListReload:function(event){
            vm.switchResultList();
            let page = $("#resultListJqGrid").jqGrid('getGridParam','page');
            $("#resultListJqGrid").jqGrid('setGridParam',{
                postData:{'name': vm.q.name,
                    'ranking1': vm.q.ranking1,
                    'ranking2': vm.q.ranking2,
                    'selectedDept': vm.q.selectedDept,
                    'selectedRating': vm.q.selectedRating},
                page:page
            }).trigger("reloadGrid");
        },
        querySummary: function(){

            let id = getSelectedRow();
            if (id == null) {
                return;
            }

            let rowData = $("#jqGrid").getRowData(id);
            vm.taskId = rowData.id;

            vm.switchResultSummary();

            // 基于准备好的dom，初始化echarts实例
            let chartDiv = document.getElementById('echart_div');
            let myChart1 = echarts.init(document.getElementById('container_e1'),'light');
            let myChart2 = echarts.init(document.getElementById('container_e2'),'light');
            let myChart3 = echarts.init(document.getElementById('container_e3'),'light');
            let myChart4 = echarts.init(document.getElementById('container_e4'),'light');

            let myChart2SeriesLabel = {
                normal: {
                    show: true,
                    textBorderColor: '#333',
                    textBorderWidth: 2
                }
            }

            $.get(baseURL + 'eval/evalresult/detail/summary/'+id, function(r){
                let summary = r.summary;
                myChart1.setOption({
                    title : {
                        text: '优秀/良好/合格占比分析',
                        x: 'center'
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: ['优秀','良好','合格','不合格']
                    },
                    series : [
                        {
                            name: '优秀/良好/合格占比',
                            type: 'pie',
                            radius: '55%',
                            roseType: 'angle',
                            data:[
                                {value:summary.goodCount, name:'优秀'},
                                {value:summary.fineCount, name:'良好'},
                                {value:summary.passCount, name:'合格'},
                                {value:summary.failCount, name:'不合格'}
                            ]
                        }
                    ]
                });

                myChart2.setOption({
                    title: {
                        text: '各系部平均分分析'
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    legend: {
                        data: ['学生评价','同行评价','督导评价','其他评价','总分']

                    },
                    grid: {
                        left: 100
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    xAxis: {
                        type: 'value',
                        name: '分数',
                        axisLabel: {
                            formatter: '{value}'
                        }
                    },
                    yAxis: {
                        type: 'category',
                        inverse: true,
                        data: summary.deptList
                    },
                    series: [
                        {
                            name: '学生评价',
                            type: 'bar',
                            label: myChart2SeriesLabel,
                            data: summary.deptAverageStudentList
                        },
                        {
                            name: '同行评价',
                            type: 'bar',
                            label: myChart2SeriesLabel,
                            data: summary.deptAverageColleagueList
                        },
                        {
                            name: '督导评价',
                            type: 'bar',
                            label: myChart2SeriesLabel,
                            data: summary.deptAverageInspectorList
                        },
                        {
                            name: '其他评价',
                            type: 'bar',
                            label: myChart2SeriesLabel,
                            data: summary.deptAverageOtherList
                        },
                        {
                            name: '总分',
                            type: 'bar',
                            label: myChart2SeriesLabel,
                            data: summary.deptAverageTotalList
                        }
                    ]

                });

                myChart3.setOption({
                    title: {
                        text: '评价标准分析'
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    tooltip: {},
                    legend: {
                        data:['平均分','最高分','最低分']
                    },
                    xAxis: {
                        data: ['学生评价','同行评价','督导评价','其他评价','总分']
                    },
                    yAxis: {},
                    series: [
                        {
                            name: '平均分',
                            type: 'bar',
                            data: [summary.averageStudent, summary.averageColleague, summary.averageInspector, summary.averageOther, summary.averageTotal]
                        },
                        {
                            name: '最高分',
                            type: 'bar',
                            data: [summary.maxStudent, summary.maxColleague, summary.maxInspector, summary.maxOther, summary.maxTotal]
                        },
                        {
                            name: '最低分',
                            type: 'bar',
                            data: [summary.minStudent, summary.minColleague, summary.minInspector, summary.minOther, summary.minTotal]
                        }]
                });
                let minScore = Math.min(summary.minStudent, summary.minColleague, summary.minInspector, summary.minOther, summary.minTotal);

                myChart4.setOption({
                    title: {
                        text: '学院个人成绩分析',
                        top: 10,
                        left: 10
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    tooltip: {
                        trigger: 'item',
                        backgroundColor : 'rgba(100,149,237,0.8)'
                    },
                    legend: {
                        type: 'scroll',
                        bottom: 10,
                        data: summary.personList
                    },
                    visualMap: {
                        top: 'middle',
                        right: 10,
                        type: 'continuous',
                        min: 60,
                        max: 100,
                        text:['High','Low'],
                        realtime: false,
                        calculable : true,
                        color: ['orangered','yellow','lightskyblue']
                    },
                    radar: {
                        indicator : [
                            { text: '总分', min: 60, max: 100},
                            { text: '学生评价', min: 60, max: 100},
                            { text: '同行评价', min: 60, max: 100},
                            { text: '督导评价', min: 60, max: 100},
                            { text: '其他评价', min: 60, max: 100}
                        ]
                    },
                    series : (function () {
                        let series = [];
                        for(let n = 0 ; n < summary.personList.length ; ++n){
                            let item = {
                                name: summary.personList[n],
                                type: 'radar',
                                symbol: 'none',
                                lineStyle: {
                                    width: 1
                                },
                                emphasis: {
                                    areaStyle: {
                                        color: 'rgba(0,250,0,0.6)'
                                    }
                                },
                                data: [{
                                    value:[summary.personTotalList[n],summary.personStudentList[n],summary.personColleagueList[n],summary.personInspectorList[n],summary.personOtherList[n]],
                                    name: summary.personList[n]
                                }]
                            };
                            series.push(item);
                        }
                        return series;
                    })()
                });

                for(let i = 0 ; i < summary.deptDetails.length ; ++i){
                    let echartId = 'container_e_dept_'+i;
                    let deptDetail = summary.deptDetails[i];

                    let containerDiv = document.createElement("div");
                    containerDiv.setAttribute('style','width: 1000px;height:400px;');
                    containerDiv.setAttribute('id',echartId);
                    let formGroupDiv = document.createElement("div");
                    formGroupDiv.setAttribute("class", "form-group");
                    formGroupDiv.appendChild(containerDiv);
                    chartDiv.appendChild(formGroupDiv);

                    let myChart = echarts.init(containerDiv,'light');
                    myChart.setOption({
                        title: {
                            text: '系部个人成绩分析',
                            subtext: deptDetail.deptName,
                            top: 10,
                            left: 10
                        },
                        toolbox: {
                            show: true,
                            feature: {
                                saveAsImage: {}
                            }
                        },
                        tooltip: {
                            trigger: 'item',
                            backgroundColor : 'rgba(100,149,237,0.8)'
                        },
                        legend: {
                            type: 'scroll',
                            bottom: 10,
                            data: deptDetail.personList
                        },
                        visualMap: {
                            top: 'middle',
                            right: 10,
                            type: 'continuous',
                            min: 60,
                            max: 100,
                            text:['High','Low'],
                            realtime: false,
                            calculable : true,
                            color: ['orangered','yellow','lightskyblue']
                        },
                        radar: {
                            indicator : [
                                { text: '总分', min: 60, max: 100},
                                { text: '学生评价', min: 60, max: 100},
                                { text: '同行评价', min: 60, max: 100},
                                { text: '督导评价', min: 60, max: 100},
                                { text: '其他评价', min: 60, max: 100}
                            ]
                        },
                        series : (function () {
                            let series = [];
                            for(let n = 0 ; n < deptDetail.personList.length ; ++n){
                                let item = {
                                    name: deptDetail.personList[n],
                                    type: 'radar',
                                    symbol: 'none',
                                    lineStyle: {
                                        width: 1
                                    },
                                    emphasis: {
                                        areaStyle: {
                                            color: 'rgba(0,250,0,0.6)'
                                        }
                                    },
                                    data: [{
                                            value:[deptDetail.personTotalList[n],deptDetail.personStudentList[n],deptDetail.personColleagueList[n],deptDetail.personInspectorList[n],deptDetail.personOtherList[n]],
                                            name: deptDetail.personList[n]
                                        }]
                                };
                                series.push(item);
                            }
                            return series;
                        })()
                    });
                }
            });
        },
        exportEvalResult: function(event){
            let url = baseURL + 'eval/evalresult/detail/export/'+vm.taskId;
            let form = $("<form></form>").attr("action", url).attr("method", "post");
            form.append($("<input></input>").attr("type", "hidden").attr("name", "name").attr("value", vm.q.name));
            form.append($("<input></input>").attr("type", "hidden").attr("name", "ranking1").attr("value", vm.q.ranking1));
            form.append($("<input></input>").attr("type", "hidden").attr("name", "ranking2").attr("value", vm.q.ranking2));
            form.append($("<input></input>").attr("type", "hidden").attr("name", "selectedDept").attr("value", vm.q.selectedDept));
            form.append($("<input></input>").attr("type", "hidden").attr("name", "selectedRating").attr("value", vm.q.selectedRating));
            form.appendTo('body').submit().remove();
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
            vm.showSummary = false;
            vm.showResultUpdate = false;
        },
        switchResultList: function () {
            vm.showList = false;
            vm.showResultList = true;
            vm.showSummary = false;
            vm.showResultUpdate = false;
        },
        switchResultSummary: function () {
		    vm.title = "图表分析汇总";
            vm.showList = false;
            vm.showResultList = false;
            vm.showSummary = true;
            vm.showResultUpdate = false;
        },
        switchResultUpdate: function () {
            vm.showList = false;
            vm.showResultList = false;
            vm.showSummary = false;
            vm.showResultUpdate = true;
        }
	}
});