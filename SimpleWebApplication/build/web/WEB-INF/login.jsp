<%-- 
    Document   : Login
    Created on : Apr 12, 2020, 12:31:48 PM
    Author     : 797138
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>
    </head>
    <body>
        <h1>HOME nVentory</h1>
        <form method="POST" action="login">
            <h2>login</h2>
            Username: <input type="text" required name="username" value="${username}"><br>
            password: <input type="password" required name="password"><br>
            <input type="submit" value="login">
            <a href="forgotPassword">Forgot Password?</a><br>
            <c:if test="${passIncorrect}">
                <p>Incorrect Password!<p>
            </c:if>
            <c:if test="${userNameIncorrect}">
                <p>Incorrect Username!</p>
            </c:if>
            <c:if test="${isActive}">
                <p>Account is Inactive!</p>
            </c:if>
            ${logOutMessage}
            <c:if test="${deactivated}">
                <p>Your account has successfully been deactivated!</p>
            </c:if>
            ${activatedMessage}
        </form>
        <h2>Register</h2>
        <form method="POST" action="login">
            Username: <input type="text" required name="userNameRegister" value="${usernameRegister}" maxlength="10"><br>
            Password: <input type="password" required name="passwordRegister"><br>
            Confirm Password: <input type="password" required name="passwordRegisterConfirm" maxlength="20"><br>
            First Name: <input type="text" required name="firstNameRegister" value="${firstNameRegister}" maxlength="50"><br>
            Last Name: <input type="text" required name="lastNameRegister" value="${lastNameRegister}" maxlength="50"><br>
            Email: <input type="email" required name="emailRegister" value="${emailRegister}" maxlength="50"><br>
            <input type="submit" value="register" name="register"><br>
        </form>
            
            <c:if test="${userNameUsed || emailUsed || passwordUnequal}">
                <p>One or more of the following issue(s) were encountered: </p>
                <ul>
                    <c:if test="${userNameUsed}">
                        <li>Username is already in use</li>
                    </c:if>
                    <c:if test="${passwordUnequal}">
                        <li>Passwords do not equal</li>
                    </c:if>
                    <c:if test="${emailUsed}">
                        <li>Email is already in use</li>
                    </c:if>
                </ul>
            </c:if>
            
    </body>
</html>
