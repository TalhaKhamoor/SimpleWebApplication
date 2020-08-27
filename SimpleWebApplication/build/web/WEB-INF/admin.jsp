<%-- 
    Document   : admin
    Created on : Apr 12, 2020, 10:21:27 PM
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
        <h1>Admin for ${fullName}</h1>
        <ul>
            <li> <a href="inventory">Inventory</a></li>
            <li> <a href="login?logout">Logout</a></li>
        </ul><br>
        ${adminErrorMessage}
        <h2>Search For Item</h2>
        <form method="post" action="admin">
            <input type="text" required name="searchItem" onkeyup="submit>
            <input type="submit" value="search">
            <input type="hidden" name="action" value="search"><br>
          ${searchResult}
        </form>
        <h2>Manage Users</h2>
            <table border="1">
                <tr>
                    <th>Username</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Admin Status</th>
                    <th>Active Status</th>
                    <th>Promote</th>
                    <th>Demote</th>
                    <th>Activate</th>
                    <th>Edit</th>
                    <th>Delete</th>

                </tr>
                <c:forEach var="Users" items="${users}">
                    <tr>
                        <td>${Users.username}</td>
                        <td>${Users.firstName}</td>
                        <td>${Users.lastName}</td>
                        <td>${Users.isAdmin}</td>
                        <td>${Users.active}</td>
                        <td>
                            <form action="admin" method="post">
                                <input type="submit" value="Make Admin">
                                <input type="hidden" name="action" value="makeAdmin">
                                <input type="hidden" name="selectedUsername" value="${Users.username}">
                            </form>
                        </td>
                        <td>
                            <form action="admin" method="post">
                                <input type="submit" value="Demote Admin">
                                <input type="hidden" name="action" value="demoteAdmin">
                                <input type="hidden" name="selectedUsername" value="${Users.username}">
                            </form>
                        </td>
                        <td>
                            <form action="admin" method="post">
                                <input type="submit" value="Activate">
                                <input type="hidden" name="action" value="activate">
                                <input type="hidden" name="selectedUsername" value="${Users.username}">
                            </form>
                        </td>
                        <td>
                            <form action="admin" method="get">
                                <input type="submit" value="Edit">
                                <input type="hidden" name="action" value="edit">
                                <input type="hidden" name="selectedUsername" value="${Users.username}">
                            </form>
                        </td>
                        <td>
                            <form action="admin" method="post">
                                <input type="submit" value="Delete">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="selectedUsername" value="${Users.username}">
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        <p>${errorMessage}</p>
        <h2>${heading}</h2>
        <c:if test="${selectedUsername != null}">
            <form action="admin" method="post">
            Username: <input type="text" name="username" value="${selectedUsername.username}" readonly><br>
            Password: <input type="text" name="password" value="${selectedUsername.password}"><br>
            Email: <input type="text" name="email" value="${selectedUsername.email}"><br>
            First Name: <input type="text" name="firstname" value="${selectedUsername.firstName}"><br>
            Last Name: <input type="text" name="lastname" value="${selectedUsername.lastName}"><br>
            <input type="submit" value="Save">
            <input type="hidden" name="action" value="save">
            </form>
        </c:if>
        <c:if test="${selectedUsername == null}">
            <form action="admin" method="post">
            Username: <input type="text" name="username"><br>
            Password: <input type="text" name="password"><br>
            Email: <input type="text" name="email"><br>
            First Name: <input type="text" name="firstname"><br>
            Last Name: <input type="text" name="lastname"><br>
            <input type="submit" value="Save">
            <input type="hidden" name="action" value="save">
            </form>
        </c:if>
        <h2>Categories</h2>
        <table border="1">
            <tr>
                <td>Category</td>
                <td>Edit</td>
                <td>Delete</td>
            </tr>
            <c:forEach var="Categories" items="${categories}">
                <tr>
                    <td>${Categories.categoryName}</td>
                    <td>
                       <form action="admin" method="post">
                            <input type="text" name="newCategoryName" required>
                            <input type="submit" value="Edit">
                            <input type="hidden" name="action" value="editCategory">
                            <input type="hidden" name="selectedCategory" value="${Categories.categoryID}">
                        </form> 
                    </td>
                    <td>
                       <form action="admin" method="post">
                            <input type="submit" value="Delete">
                            <input type="hidden" name="action" value="deleteCategory">
                            <input type="hidden" name="selectedCategory" value="${Categories.categoryID}">
                        </form> 
                    </td>
                </tr>
            </c:forEach>
        </table>
        <h3>Add New Category</h3>
        <form method="POST" action="admin">
            Category Name: <input type="text" name="addCategory" required>
            <input type="submit" name="action" value="add">
        </form>
        
        
    </body>
</html>
