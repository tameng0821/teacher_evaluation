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
	let parentId = data[0].parentId;
	data.forEach( (current)=>{
		if(current.parentId < parentId){
			parentId = current.parentId;
		}
	} );
	//没有父节点的数据
	let parents = data.filter(value => value.parentId === parentId);
	//有父节点的数据
	let children = data.filter(value => value.parentId !== parentId);
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
			if(current.deptId == deptId){
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
//选择部门时部门树设置
let deptZtreeSetting = {
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
//从后台获取部门数据
function getDeptTreeData(callback) {
	$.get(baseURL + "sys/dept/list", function (r) {
		callback(r);
	});
}
//从后台获取当前部门所有用户数据
function getUserListData(deptId,callback) {
	$.get(baseURL + "sys/user/list/" + deptId, function (r) {
		if (r.code === 0) {
			callback(r.list);
		} else {
			console.log(r.msg);
		}
	})
}

/**
 *
 * 初始化部门ztree
 * @param element  html元素 比如$("#deptTree")
 * @param setting 配置
 * @param deptData 部门数据，不能是从ajax获取的因为有延迟
 * @returns ztree
 */
function initDeptTree(element,setting,deptData) {
	let deptTree =  $.fn.zTree.init(element, setting, deptData);
	deptTree.expandAll(true);
	return deptTree;
}
/**
 *
 * 初始化部门选择功能时的部门ztree
 * @param element  html元素 比如$("#deptTree")
 * @param deptData 部门数据，不能是从ajax获取的因为有延迟
 * @returns ztree
 */
function initDeptSelectDeptTree(element,deptData) {
	return initDeptTree(element,deptZtreeSetting,deptData);
}

/**
 *  初始化添加用户功能时的部门ztree
 * @param element html元素 比如$("#deptTree")
 * @param deptData 部门数据，不能是从ajax获取的因为有延迟
 * @param callback 树状图节点点击触发事件
 * @returns ztree
 */
function initUserSelectDeptTree(element,deptData,callback) {
	//选择人员时部门树设置
	let treeSetting = {
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
			onClick: function (event, treeId, treeNode) {
				callback(treeNode.deptId);
			}
		}
	};
	return initDeptTree(element,treeSetting,deptData);
}

/**
 *  显示用户选择弹窗
 * @param element html元素 比如$("#deptTree")
 * @param deptData 部门数据，不能是从ajax获取的因为有延迟
 * @param deptId 部门ID
 * @param title layer标题
 * @param content layer内容，比如 jQuery("#userLayer")
 * @param getUserCallback 树状图节点点击回调函数
 * @param confirmCallback layer点击确认回调函数
 */
function showSelectUserLayer(element,deptData,deptId,title,content,getUserCallback,confirmCallback) {

	initUserSelectDeptTree(element,deptData,getUserCallback);

	getUserCallback(deptId);

	layer.open({
		type: 1,
		offset: '50px',
		skin: 'layui-layer-molv',
		title: title,
		area: ['800px', '410px'],
		shade: 0,
		shadeClose: false,
		content: content,
		btn: ['确定', '取消'],
		btn1: function (index) {
			confirmCallback(index);
		}
	});
}

//子页面内更改父页面地址
function contentFrameLoad(url) {
	$('#contentFrame',window.parent.document)[0].src = url;
}

//选择一条记录
function getRecordListSelectedRow() {
	let grid = $("#recordListJqGrid");
	let rowKey = grid.getGridParam("selrow");
	if(!rowKey){
		alert("请选择一条记录");
		return ;
	}

	let selectedIDs = grid.getGridParam("selarrrow");
	if(selectedIDs.length > 1){
		alert("只能选择一条记录");
		return ;
	}

	return selectedIDs[0];
}

//选择多条记录
function getRecordListSelectedRows() {
	let grid = $("#recordListJqGrid");
	let rowKey = grid.getGridParam("selrow");
	if(!rowKey){
		alert("请选择一条记录");
		return ;
	}

	return grid.getGridParam("selarrrow");
}

//初始化fileinput
let fileInputInit = function () {

};