<%-- 
    Document   : forgotPassword
    Created on : Apr 15, 2020, 7:08:01 PM
    Author     : 797138
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Forgot Password</title>
    </head>
    <body>
        <form method="post" action="forgotPassword">
            <h1>Forgot your Password?</h1>
            <h2>No worries, just enter your email!</h2>
            <input type="text" name="emailForgot">
            <input type="submit" value="send">
            <input type="hidden" name="action" value="forgotPassword">
        </form>
        ${message}
    </body>
</html>
