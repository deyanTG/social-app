<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title></title>
<link rel="stylesheet" type="text/css"
	href="/css/bootstrap-theme.css">
<link rel="stylesheet" type="text/css"
	href="/css/bootstrap.css">
<link rel="stylesheet" type="text/css"
	href="/css/font-awesome.css">
<link rel="stylesheet" type="text/css"
	href="/css/social-buttons-3.css">
</head>
<body>
	<div class="panel panel-default">
		<div class="panel-body">
			<h2>Login Form</h2>
			<form name='loginForm' action=""
				method="POST" role="form">
				<div class="row">
					<div id="form-group-email" class="form-group col-lg-4">
						<label class="control-label" for="user-email">Username:</label> <input
							id="user-email" name="username" type="text" class="form-control" />
					</div>
				</div>

				<div class="row">
					<div id="form-group-password" class="form-group col-lg-4">
						<label class="control-label" for="user-password">Password:</label>
						<input id="user-password" name="password" type="password"
							class="form-control" />
					</div>
				</div>
				<div class="row">
					<div class="form-group col-lg-4">
						<button type="submit" class="btn btn-default">Sign in</button>
					</div>
				</div>
			</form>
			<div class="row">
				<div class="form-group col-lg-4">
					<a href="${pageContext.request.contextPath}/user/register">Sign
						up</a>
				</div>
			</div>
		</div>
	</div>
	<div class="panel panel-default">
		<div class="panel-body">
			<h2>Social Sign Up</h2>
			<div class="row social-button-row">
				<div class="col-lg-4">
					<a href="${pageContext.request.contextPath}/auth/linkedin"><button
							class="btn btn-linkedin">
							<i class="icon-linkedin"></i>Linkedin
						</button></a>
				</div>
			</div>
			
			<div class="row social-button-row">
				<div class="col-lg-4">
					<a href="${pageContext.request.contextPath}/auth/google"><button
							class="btn btn-google">
							<i class="icon-google"></i>Google
						</button></a>
				</div>
			</div>
			
			<div class="row social-button-row">
				<div class="col-lg-4">
					<a href="${pageContext.request.contextPath}/auth/facebook"><button
							class="btn btn-facebook">
							<i class="icon-facebook"></i>Facebook
						</button></a>
				</div>
			</div>
		</div>
	</div>
</body>
</html>