<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Магазин</title>
    <%@ include file="/WEB-INF/jsp/fragments/head.jspf" %>
</head>
<body>
<%@ include file="/WEB-INF/jsp/fragments/navbar.jspf" %>

<div class="container app-shell">
    <div class="row g-3">
        <div class="col-12 col-lg-7">
            <div class="glass">
                <div class="glass-header">
                    <h2 class="h5 mb-1">Каталог</h2>
                    <div class="muted">Добавляйте товары в корзину, если они есть на складе.</div>
                </div>
                <div class="glass-body">

                    <c:if test="${not empty requestScope.error}">
                        <div class="alert alert-danger mb-3">${requestScope.error}</div>
                    </c:if>

                    <c:if test="${not empty requestScope.msg}">
                        <div class="alert alert-success mb-3">${requestScope.msg}</div>
                    </c:if>

                    <div class="table-responsive">
                        <table class="table align-middle mb-0">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Товар</th>
                                <th class="text-end">Цена</th>
                                <th class="text-center">Остаток</th>
                                <th class="text-end">Действие</th>
                            </tr>
                            </thead>
                            <tbody>

                            <c:forEach var="p" items="${applicationScope.catalog.values()}">
                                <tr>
                                    <td class="muted">${p.id}</td>
                                    <td>
                                        <div class="fw-semibold">${p.name}</div>
                                        <div class="muted small">Артикул: ${p.id}</div>
                                    </td>
                                    <td class="text-end">${p.price}</td>
                                    <td class="text-center">
                                        <c:choose>
                                            <c:when test="${p.stock gt 0}">
                                                <span class="pill">В наличии: <b>${p.stock}</b></span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="pill pill-danger">Нет в наличии</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-end">
                                        <c:choose>
                                            <c:when test="${p.stock gt 0}">
                                                <form method="post" action="${pageContext.request.contextPath}/shop" class="m-0 d-inline">
                                                    <input type="hidden" name="action" value="add">
                                                    <input type="hidden" name="id" value="${p.id}">
                                                    <button class="btn btn-primary btn-sm" type="submit">Добавить</button>
                                                </form>
                                            </c:when>
                                            <c:otherwise>
                                                <button class="btn btn-outline-light btn-sm" disabled>Недоступно</button>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>

                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-12 col-lg-5">
            <div class="glass">
                <div class="glass-header">
                    <h2 class="h5 mb-1">Корзина</h2>
                    <div class="muted">Товары из вашей сессии.</div>
                </div>
                <div class="glass-body">

                    <c:choose>
                        <c:when test="${empty sessionScope.cart}">
                            <div class="muted">Корзина пуста.</div>
                        </c:when>
                        <c:otherwise>

                            <c:if test="${not empty requestScope.cartCount}">
                                <div class="muted mb-2">Позиций: <b>${requestScope.cartCount}</b>, сумма: <b>${requestScope.cartTotal}</b></div>
                            </c:if>

                            <div class="table-responsive">
                                <table class="table align-middle mb-0">
                                    <thead>
                                    <tr>
                                        <th>Товар</th>
                                        <th class="text-center">Кол-во</th>
                                        <th class="text-end">Сумма</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="ci" items="${sessionScope.cart.values()}">
                                        <tr>
                                            <td>
                                                <div class="fw-semibold">${ci.productName}</div>
                                                <div class="muted small">${ci.price} за шт.</div>
                                            </td>
                                            <td class="text-center">${ci.qty}</td>
                                            <td class="text-end">${ci.lineTotal}</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>

                            <div class="d-flex gap-2 mt-3">
                                <form method="post" action="${pageContext.request.contextPath}/shop" class="m-0 flex-fill">
                                    <input type="hidden" name="action" value="checkout">
                                    <button class="btn btn-primary w-100" type="submit">Оформить</button>
                                </form>

                                <form method="post" action="${pageContext.request.contextPath}/shop" class="m-0">
                                    <input type="hidden" name="action" value="clear">
                                    <button class="btn btn-outline-light" type="submit">Очистить</button>
                                </form>
                            </div>

                        </c:otherwise>
                    </c:choose>

                </div>
            </div>
        </div>

    </div>
</div>

</body>
</html>
