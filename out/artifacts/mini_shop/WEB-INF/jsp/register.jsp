<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Регистрация</title>
    <%@ include file="/WEB-INF/jsp/fragments/head.jspf" %>
</head>
<body>
<%@ include file="/WEB-INF/jsp/fragments/navbar.jspf" %>

<div class="container app-shell container-narrow">
    <div class="glass">
        <div class="glass-header">
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <h1 class="h4 mb-1">Регистрация</h1>
                    <div class="muted">Создайте аккаунт для покупок.</div>
                </div>
                <span class="badge-soft rounded-pill px-3 py-2">New</span>
            </div>
        </div>

        <div class="glass-body">
            <c:if test="${not empty error}">
                <div class="alert alert-danger mb-3">${error}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/register">
                <div class="mb-3">
                    <label class="form-label">Имя пользователя</label>
                    <input class="form-control" name="username" required autocomplete="username">
                </div>

                <div class="mb-3">
                    <label class="form-label">Email</label>
                    <input class="form-control" type="email" name="email" required autocomplete="email">
                </div>

                <div class="mb-2">
                    <label class="form-label">Пароль</label>
                    <input class="form-control" type="password" name="password" required autocomplete="new-password">
                    <div class="form-text">Минимум 6 символов.</div>
                </div>

                <button class="btn btn-primary w-100 py-2 mt-2" type="submit">Создать аккаунт</button>
            </form>
        </div>

        <div class="glass-footer">
            <div class="footer-note">
                Уже есть аккаунт? <a href="${pageContext.request.contextPath}/login">Войти</a>
            </div>
        </div>
    </div>
</div>

</body>
</html>
