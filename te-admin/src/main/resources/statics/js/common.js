//jqGrid的配置信息
$.jgrid.defaults.width = 1000;
$.jgrid.defaults.responsive = true;
$.jgrid.defaults.styleUI = 'Bootstrap';

var baseURL = "../../";

//工具集合Tools
window.T = {};

// 获取请求参数
// 使用示例
// location.href = http://localhost:8080/index.html?id=123
// T.p('id') --> 123;
var url = function(name) {
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null)return  unescape(r[2]); return null;
};
T.p = url;

//全局配置
$.ajaxSetup({
	dataType: "json",
	cache: false
});

//重写alert
window.alert = function(msg, callback){
	parent.layer.alert(msg, function(index){
		parent.layer.close(index);
		if(typeof(callback) === "function"){
			callback("ok");
		}
	});
}

//重写confirm式样框
window.confirm = function(msg, callback){
	parent.layer.confirm(msg, {btn: ['确定','取消']},
	function(){//确定事件
		if(typeof(callback) === "function"){
			callback("ok");
		}
	});
}

//选择一条记录
function getSelectedRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
    	alert("请选择一条记录");
    	return ;
    }
    
    var selectedIDs = grid.getGridParam("selarrrow");
    if(selectedIDs.length > 1){
    	alert("只能选择一条记录");
    	return ;
    }
    
    return selectedIDs[0];
}

//选择多条记录
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if(!rowKey){
    	alert("请选择一条记录");
    	return ;
    }
    
    return grid.getGridParam("selarrrow");
}

//判断是否为空
function isBlank(value) {
    return !value || !/\S/.test(value)
}

/**
 * 该方法用于将有父子关系的数组转换成树形结构的数组
 * 接收一个具有父子关系的数组作为参数
 * 返回一个树形结构的数组
 */
function translateDeptDataToTree(data) {
	//没有父节点的数据
	let parents = data.filter(value => value.parentId === 0);
	//有父节点的数据
	let children = data.filter(value => value.parentId !== 0);
	//定义转换方法的具体实现
	let translator = (parents, children) => {
		//遍历父节点数据
		parents.forEach((parent) => {
			//遍历子节点数据
			children.forEach((current, index) => {
				//此时找到父节点对应的一个子节点
				if(current.parentId === parent.deptId){
					//对子节点数据进行深复制，这里只支持部分类型的数据深复制，对深复制不了解的童靴可以先去了解下深复制
					let temp = JSON.parse(JSON.stringify(children));
					//让当前子节点从temp中移除，temp作为新的子节点数据，这里是为了让递归时，子节点的遍历次数更少，如果父子关系的层级越多，越有利
					temp.splice(index, 1);
					//让当前子节点作为唯一的父节点，去递归查找其对应的子节点
					translator([current], temp);
					//把找到子节点放入父节点的children属性中
					typeof parent.children !== 'undefined' ? parent.children.push(current) : parent.children = [current];
				}
			})
		})
	};
	//调用转换方法
	translator(parents, children);
	//返回最终的结果
	return parents;
}
//从树状结构的部门列表中选择当前部门
function findDeptFromTreeData(treeData,deptId) {
	let result=[];
	let findTreeData = (treeData,deptId)	=> {
		treeData.forEach( (current) => {
			if(current.deptId === deptId){
				result = current;
				return false;
			}else if(typeof current.children !== 'undefined'){
				findTreeData(current.children,deptId);
			}
		});
	};
	findTreeData(treeData,deptId);
	return result;
}