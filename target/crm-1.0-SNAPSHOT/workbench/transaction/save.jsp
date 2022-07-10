<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

	Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");

	Set<String> set = pMap.keySet();

%>

<!DOCTYPE html>
<html>
<head>

	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>


	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

	<script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>

	<script>

		var json = {

			<%

				for (String key:set){

					String value = pMap.get(key);

			%>

					"<%=key%>" : <%=value%>,

			<%
				}

			%>

		};

		//alert(json);


	$(function (){

		$("#create-accountName").typeahead({
			source: function (query, process) {
				$.get(
						"workbench/transaction/getCustomerName.do",
						{ "name" : query },
						function (data) {
							//alert(data);
							process(data);
						},
						"json"
				);
			},
			delay: 1000
		});

		//日期插件
		$(".time1").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		//日期插件
		$(".time2").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "top-left"
		});


		//为查找市场活动中的搜索框中绑定事件getActivityId
		$("#getActivityId").click(function (){

			openActivity();

		})

		//为模态窗口中的小圆圈绑定事件
		$("#bundBtn").click(function (){

			var $xz = $("input[name=activity]:checked").val();
			var name = $("#"+$xz).html();

			$("#create-activitySrc").val(name);
			$("#hidden-activity").val($xz);

			$("#findMarketActivity").modal("hide");



		})

		//为交易创建窗口中的查找联系人的提交绑定事件
		$("#contactsBtn").click(function (){

			//先获取这个选框中的信息
			var $cxz = $("input[name=ContactsOne]:checked").val();
			var cname = $("#"+$cxz).html();

			//将获得的信息展示在页面上
			$("#create-contactsName").val(cname);
			$("#hidden-contacts").val($cxz);

			//展示好了之后就关闭模态窗口
			$("#findContacts").modal("hide");


		})

		//为保持按钮绑定事件
		$("#saveBtn").click(function (){

			//发出传统请求，提交表单
			$("#TranForm").submit();


		})


		$("#getActivityId").keydown(function (event){

			if (event.keyCode == 13){
				$("#activityBody").val("");

				$.ajax({
					url:"workbench/transaction/getAllActivityByName.do",
					data:{
						"name":$.trim($("#getActivityId").val())
					},
					type:"get",
					dataType:"json",
					success:function (data){

						var html = "";

						$.each(data,function (i,n){
							html += '<tr>';
							html += '<td><input type="radio" name="activity" value="'+n.id+'"/></td>';
							html += '<td id="'+n.id+'">'+n.name+'</td>';
							html += '<td>'+n.startDate+'</td>';
							html += '<td>'+n.endDate+'</td>';
							html += '<td>'+n.owner+'</td>';
							html += '</tr>';

						})
						//拼接字符串
						$("#activityBody").html(html);
						/*//拼接字符串
                        $("#activityBody").html(html);

                        //计算总页数
                        var totalPages = data.total%pageSize == 0 ?data.total/pageSize : parseInt(data.total/pageSize) + 1;

                        //数据处理完毕之后，结合分页查询，对前端展示分页信息
                        $("#activityPage").bs_pagination({
                            currentPage: pageNo, // 页码
                            rowsPerPage: pageSize, // 每页显示的记录条数
                            maxRowsPerPage: 20, // 每页最多显示的记录条数
                            totalPages: totalPages, // 总页数
                            totalRows: data.total, // 总记录条数

                            visiblePageLinks: 3, // 显示几个卡片

                            showGoToPage: true,
                            showRowsPerPage: true,
                            showRowsInfo: true,
                            showRowsDefaultInfo: true,

                            //该回调函数是在，点击分页组件的时候触发的
                            onChangePage : function(event, data){
                                pageList(data.currentPage , data.rowsPerPage);
                            }
                        });*/
						//计算总页数
						/*var totalPages = data.total%pageSize == 0 ?data.total/pageSize : parseInt(data.total/pageSize) + 1;

                        //数据处理完毕之后，结合分页查询，对前端展示分页信息
                        $("#activityPage").bs_pagination({
                            currentPage: pageNo, // 页码
                            rowsPerPage: pageSize, // 每页显示的记录条数
                            maxRowsPerPage: 20, // 每页最多显示的记录条数
                            totalPages: totalPages, // 总页数
                            totalRows: data.total, // 总记录条数

                            visiblePageLinks: 3, // 显示几个卡片

                            showGoToPage: true,
                            showRowsPerPage: true,
                            showRowsInfo: true,
                            showRowsDefaultInfo: true,

                            //该回调函数是在，点击分页组件的时候触发的
                            onChangePage : function(event, data){
                                pageList(data.currentPage , data.rowsPerPage);
                            }
                        });*/

					}

				})


				//展现列表之后，需要将模态窗口中的默认回车键禁用
				return false;

			}


		})

		//为关联绑定事件，点击绑定把下面选中的信息提交到创建交易页面
		/*$("#bundBtn").click(function (){



			/!*$("#create-activitySrc").val();*!/

		})*/

		//为阶段的下拉框，绑定选中下拉框的事件，根据选中的阶段填写可能性
		$("#create-stage").change(function (){

			//获取选中阶段
			var stage = $("#create-stage").val();

			/*
				目标：填写可能性

				阶段有了stage
				阶段和可能性之间的对应pMap，但是pMap是Java中的键值对关系


				我们要做的是将pMap
				pMap. put("01资质审查”, 10);
				pMap. put( "02需求分析”, 25);
				转换为
				var json = {"01资质审查 ”:10,”02需求分析":25...};
				以上我们已经将json处理好了
				接下来取可能性


			 */

			/*

					我们现在以json. key的形式不能取得value
					因为今天的stage是一一个可变的变量
					如果是这样的key,那么我们就不能以传统的json. key的形式来取值
					我们要使用的取值方式为
					json[key]

			*/
			var passibility = json[stage];
			//alert(passibility);

			$("#create-possibility").val(passibility);

		})



	})

	//为市场活动源的小放大镜绑定事件
	function openActivity(pageNo,pageSize){

		//首先通过ajax到后台查询出所有的市场活动
		$.ajax({
			url:"workbench/transaction/getAllActivity.do",
			/*data:{

				"pageNo":pageNo,
				"pageSize":pageSize,
				//"name" :$.trim($("#getActivityId").val())

			},*/
			type:"get",
			dataType:"json",
			success:function (data){

				//data [{},{},{},...,{}]
				var html = "";

				$.each(data,function (i,n){

					/*
                        <tr>
                            <td><input type="radio" name="activity"/></td>
                            <td>发传单</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                            <td>zhangsan</td>
                        </tr>
                    */
					html += '<tr>';
					html += '<td><input type="radio" name="activity" value="'+n.id+'"/></td>';
					html += '<td id="'+n.id+'">'+n.name+'</td>';
					html += '<td>'+n.startDate+'</td>';
					html += '<td>'+n.endDate+'</td>';
					html += '<td>'+n.owner+'</td>';
					html += '</tr>';

				})
				//拼接字符串
				$("#activityBody").html(html);
				/*//拼接字符串
                $("#activityBody").html(html);

                //计算总页数
                var totalPages = data.total%pageSize == 0 ?data.total/pageSize : parseInt(data.total/pageSize) + 1;

                //数据处理完毕之后，结合分页查询，对前端展示分页信息
                $("#activityPage").bs_pagination({
                    currentPage: pageNo, // 页码
                    rowsPerPage: pageSize, // 每页显示的记录条数
                    maxRowsPerPage: 20, // 每页最多显示的记录条数
                    totalPages: totalPages, // 总页数
                    totalRows: data.total, // 总记录条数

                    visiblePageLinks: 3, // 显示几个卡片

                    showGoToPage: true,
                    showRowsPerPage: true,
                    showRowsInfo: true,
                    showRowsDefaultInfo: true,

                    //该回调函数是在，点击分页组件的时候触发的
                    onChangePage : function(event, data){
                        pageList(data.currentPage , data.rowsPerPage);
                    }
                });*/
				//计算总页数
				/*var totalPages = data.total%pageSize == 0 ?data.total/pageSize : parseInt(data.total/pageSize) + 1;

				//数据处理完毕之后，结合分页查询，对前端展示分页信息
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					//该回调函数是在，点击分页组件的时候触发的
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});*/

				$("#findMarketActivity").modal("show");
			}
		})


	}

	//为查找联系人的小放大镜函数
	function openContacts(){

		//打开模态窗口
		$("#findContacts").modal("show");

		$("#getContactsByName").keydown(function (event){

			if(event.keyCode == 13){

				$.ajax({
					url:"workbench/transaction/getContactsByName.do",
					data:{

						"fullname" : $.trim($("#getContactsByName").val())

					},
					type:"get",
					dataType:"json",
					success:function (data){

						var html = "";

						$.each(data,function (i,n){

							html +='<tr>';
							html +='<td><input type="radio" name="ContactsOne" value="'+n.id+'" /></td>';
							html +='<td id="'+n.id+'" >'+n.fullname+'</td>';
							html +='<td>'+n.email+'</td>';
							html +='<td>'+n.mphone+'</td>';
							html +='</tr>';

						})

						$("#contactsBody").html(html);

					}
				})

			}

			return false;

		})



	}


	</script>

</head>
<body>

	<!-- 查找市场活动 -->	
	<div class="modal fade" id="findMarketActivity" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" id="getActivityId" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable3" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
							</tr>
						</thead>
						<tbody id="activityBody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>

					</table>
				</div>

				<div id="activityPage">

				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="bundBtn" >提交</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 查找联系人 -->	
	<div class="modal fade" id="findContacts" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找联系人</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="getContactsByName" style="width: 300px;" placeholder="请输入联系人名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>邮箱</td>
								<td>手机</td>
							</tr>
						</thead>
						<tbody id="contactsBody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>--%>
						</tbody>
					</table>

					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						<button type="button" class="btn btn-primary" id="contactsBtn" >关联</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	
	<div style="position:  relative; left: 30px;">
		<h3>创建交易</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
			<button type="button" class="btn btn-default">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" action="workbench/transaction/save.do" method="post" id="TranForm" role="form" style="position: relative; top: -30px;">
		<div class="form-group">
			<label for="create-transactionOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-transactionOwner" name="owner">
					<option></option>
					<c:forEach items="${uList}" var="u">

						<option value="${u.id}" ${user.id eq u.id ? "selected" : ""}>${u.name}</option>

				  	</c:forEach>
				</select>
			</div>
			<label for="create-amountOfMoney" class="col-sm-2 control-label">金额</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-amountOfMoney" name="money">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-transactionName" name="name">
			</div>
			<label for="create-expectedClosingDate" class="col-sm-2 control-label ">预计成交日期<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control time1" id="create-expectedClosingDate" name="expectedDate">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-accountName" class="col-sm-2 control-label">客户名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-accountName" name="customerName" placeholder="支持自动补全，输入客户不存在则新建">
			</div>
			<label for="create-transactionStage" class="col-sm-2 control-label">阶段<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
			  <select class="form-control" id="create-stage" name="stage">
			  	<option></option>
			  	<c:forEach items="${stageList}" var="sa">
					<option value="${sa.value}">${sa.text}</option>
				</c:forEach>
			  </select>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionType" class="col-sm-2 control-label">类型</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-transactionType" name="type">
				  <option></option>
				  <c:forEach items="${transactionTypeList}" var="t">

					  <option value="${t.value}">${t.text}</option>

				  </c:forEach>
				</select>
			</div>
			<label for="create-possibility" class="col-sm-2 control-label">可能性</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-possibility">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-clueSource" class="col-sm-2 control-label">来源</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-clueSource" name="source">
				  <option></option>
				  <c:forEach items="${sourceList}" var="sl">
					  <option value="${sl.value}">${sl.text}</option>
				  </c:forEach>
				</select>
			</div>
			<label for="create-activitySrc" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" onclick="openActivity()"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="hidden" id="tranActivityId">
				<input type="text" class="form-control" id="create-activitySrc">
				<input type="hidden" id="hidden-activity" name="activityId">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactsName" onclick="openContacts()" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);" ><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-contactsName">
				<input type="hidden" id="hidden-contacts" name="contactsId">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-describe" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-describe" name="description"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-contactSummary" name="contactSummary"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control time2" id="create-nextContactTime" name="nextContactTime">
			</div>
		</div>
		
	</form>
</body>
</html>