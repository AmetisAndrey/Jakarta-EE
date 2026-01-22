<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход</title>
    <%@ include file="/WEB-INF/jsp/fragments/head.jspf" %>
</head>
<body>
<%@ include file="/WEB-INF/jsp/fragments/navbar.jspf" %>

<div class="container app-shell container-narrow">
    <div class="glass">
        <div class="glass-header">
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <h1 class="h4 mb-1">Вход</h1>
                    <div class="muted">Введите логин (или email) и пароль.</div>
                </div>
                <span class="badge-soft rounded-pill px-3 py-2">Auth</span>
            </div>
        </div>

        <div class="glass-body">
            <c:if test="${not empty error}">
                <div class="alert alert-danger mb-3">${error}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/login">
                <div class="mb-3">
                    <label class="form-label">Логин или Email</label>
                    <input class="form-control" name="login" required autocomplete="username">
                </div>

                <div class="mb-3">
                    <label class="form-label">Пароль</label>
                    <input class="form-control" type="password" name="password" required autocomplete="current-password">
                </div>

                <button class="btn btn-primary w-100 py-2" type="submit">Войти</button>
            </form>
        </div>

        <div class="glass-footer">
            <div class="footer-note">
                Нет аккаунта? <a href="${pageContext.request.contextPath}/register">Создать</a>
            </div>
        </div>
    </div>
</div>

</body>
</html>
