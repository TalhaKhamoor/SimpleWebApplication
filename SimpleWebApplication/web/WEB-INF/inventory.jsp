<%-- 
    Document   : inventory
    Created on : Apr 12, 2020, 10:21:41 PM
    Author     : 797138
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Inventory for ${fullName}</h2></h1>
        <h2>menu</h2>
         <ul>
            <li> <a href="admin">admin</a></li>
            <li> <a href="login?logout">Logout</a></li>
        </ul>
        ${adminErrorMessage}
        <h2>Edit User Information</h2>
        <form method="POST" action="inventory">
            Username: <input type="text" required name="userNameEdit" value="${selectedUsername.username}" readonly><br>
            Old password: <input type="password" required name="passwordOldEdit"><br>
            New Password: <input type="password" required name="passwordNewEdit" maxlength="20"><br>
            Confirm New Password: <input type="password" required name="passwordConfirmEdit" maxlength="20"><br>
            First Name: <input type="text" required name="firstNameRegister" value="${selectedUsername.firstName}" maxlength="50"><br>
            Last Name: <input type="text" required name="lastNameRegister" value="${selectedUsername.lastName}" maxlength="50"><br>
            Email: <input type="email" required name="emailRegister" value="${selectedUsername.email}" maxlength="50"><br>
            <input type="submit" value="edit"><br>
            <input type="hidden" name="action" value="edit">
            <c:choose>
                <c:when test="${editOldNewPassUnequal}">
                    <p>User was not edited: wrong password!</p>
                </c:when>
                <c:when test="${editWrongPass}">
                    <p>User was not edited: password mismatch!</p>
                </c:when>
                <c:when test="${userEdited}">
                    <p>User successfully Edited!</p>
                </c:when>
            </c:choose>
        </form>
        <h2>Deactivate Account</h2>
        <form method="POST" action="inventory">
            <p>Warning: Once you deactivate your account, only an administrator may reactivate it!<p>
            <p>Please enter your password again to deactivate you account</p>
            <input type="password" name="deactivePass" required><br>
            <input type="submit" value="Deactivate">
            <input type="hidden" name="action" value="deactivate">
            <c:if test="${deactivateError}">
               <br>Password Incorrect!
            </c:if>
        </form>
        <h2>Inventory for ${fullName}</h2>
        <c:if test="${itemList.size() != 0}">
            <table border="1">
                <tr>
                    <th>Category</th>
                    <th>Name</th>
                    <th>Price</th>
                    <th></th>
                    <th></th>
                </tr>
                    <c:forEach var="Items" items="${itemList}">
                    <tr>
                        <td>${Items.category.categoryName}</td>
                        <td>${Items.itemName}</td>
                        <td><fmt:formatNumber value="${Items.price}" type="currency"/></td>
                        <td>
                            <form action="inventory" method="post">
                                <input type="submit" value="Delete">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="selectedItem" value="${Items.itemID}">
                            </form>
                        </td>
                        <td>
                            <form action="inventory" method="post">
                                <input type="submit" value="Edit">
                                <input type="hidden" name="action" value="editItem">
                                <input type="hidden" name="selectedItem" value="${Items.itemID}">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
        <h2>${addEditHeading}</h2>
        <form action="inventory" method="post">
            Category:
            <select name="category">
                <c:forEach var="Categories" items="${categoryList}">
                    <c:choose>
                        <c:when test="${Categories.categoryName == categorySelected}">
                            <option value="${Categories.categoryID}" selected>${Categories.categoryName}</option>
                        </c:when>
                        <c:when test="${Categories.categoryName != categorySelected}">
                            <option value="${Categories.categoryID}">${Categories.categoryName}</option>
                        </c:when>
                    </c:choose>
                </c:forEach>
            </select><br>
            Name: <input type="text" name="itemName" value="${itemName}" required><br>
            Price: <input min="0" type="number" name="price"value="${price}" required><br>
            <input type="submit" value="Save" required>
            <input type="hidden" name="action" value="save">
        </form>       
    </body>
</html>
