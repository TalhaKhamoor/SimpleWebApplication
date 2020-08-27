<%-- 
    Document   : resetPassword
    Created on : Apr 15, 2020, 8:05:46 PM
    Author     : 797138
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Reset Password</h1>
        <p>Complete the following form to reset your password</p>
        <form method="POST" action="resetPassword">
            New Password: <input type="password" name="password"><br>
            Confirm New Password: <input type="password" name="passwordConfirm"><br>
            <input type="submit" value="Change Password">
            <input type="hidden" name="action" value="resetPassword">
        </form>
        ${Message}
    </body>
</html>
