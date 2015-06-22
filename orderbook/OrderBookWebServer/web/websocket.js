window.onload = init;
var socket = new WebSocket("ws://localhost:8080/OrderBookWebServer/actions");
socket.onmessage = onMessage;
function onMessage(event) {
    var order = JSON.parse(event.data);
    switch (order.action) {

        case "addo":
            printOrder(order);
            break;
        case "placed":
            placedOrder(order);
            break;
        case "removed":
            removeOrderElement(order);
            break;
        case "fill":
            fillOrder(order);
            break;
        case "cancelled":
            printOrder(order, 'filled');
            break;
        case "position":
            updatePositions(order);
            break;
    }

}

function printOrder(order, filled) {
    if (filled === 'filled') {
        $("#orders").find(".order_" + order.id).remove();
        fillOrder(order);
    }
    var orders_content = $("#orders");
    var orderDiv = $('<div/>');
    orderDiv.addClass("order_" + order.id);
    orderDiv.addClass("order_side_" + order.side);
    orders_content.append(orderDiv);
    var orderBook = $('<span id=book/>');
    orderBook.html('<b>Book:</b> ' + order.book);
    orderDiv.append(orderBook);
    var orderSide = $('<span id=side/>');
    orderSide.html('<b>Side:</b> ' + order.side);
    orderDiv.append(orderSide);
    var orderPrice = $('<span id=price/>');
    orderPrice.html('<b>Price:</b> ' + order.price);
    orderDiv.append(orderPrice);
    var orderSize = $('<span id=size/>');
    orderSize.html('<b>Size:</b> ' + order.size);
    orderDiv.append(orderSize);
    var placeOrder = $('<span id=place/>');
    placeOrder.addClass("placeOrder");
    placeOrder.html("<a href=\"#\" OnClick=placeOrder(" + order.id + ")>Place the order</a>");
    orderDiv.append(placeOrder);
    var removeOrder = $('<span id=remove/>');
    removeOrder.addClass("removeOrder");
    removeOrder.html("<a href=\"#\" OnClick=removeOrder(" + order.id + ")>Remove the order</a>");
    orderDiv.append(removeOrder);
    if (filled === 'filled') {
        fillOrder(order);
    }
}

function placedOrder(order) {
    var order_element = $("#orders").find(".order_" + order.id);
    order_element.find('.placeOrder').remove();
    order_element.find('.removeOrder').remove();
    var cancelRemainingOrder = $('<span id=cancel/>');
    cancelRemainingOrder.addClass("cancelRemainingOrder");
    cancelRemainingOrder.html("<a href=\"#\" OnClick=cancelRemainingOrder(" + order.id + ")>Cancel remaining</a>");
    order_element.append(cancelRemainingOrder);
    var progressElement = $('<span id=progress>');
    progressElement.addClass("progressOrder");
    var percent_str = '0 / ' + order.size;
    progressElement.html('\
        <div class="progress"> \n\
            <div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width:100%"> \n\
                ' + percent_str + ' \n\
            </div>\n\
        </div>');
    order_element.append(progressElement);
}

function fillOrder(order) {
    var order_element = $("#orders").find(".order_" + order.id);
    order_element.find("#fill").remove();
    order_element.find("#progress").remove();
    var filledOrder = $("<span id=fill/>");
    filledOrder.addClass("filledOrder");
    if (order.filled === "no") {
        filledOrder.html('<b>Fill:</b> ' + order.fill_size + "/" + order.size);
        var percent = 100 * parseInt(order.fill_size) / parseInt(order.size);
        var percent_str = order.fill_size + ' / ' + order.size;
    } else {
        filledOrder.html('<b>Fill:</b> FILLED');
        percent = 100;
        percent_str = "filled";
    }

    order_element.append(filledOrder);
    var progressElement = $('<span id=progress>');
    progressElement.addClass("progressOrder");
    progressElement.html('\
        <div class="progress"> \n\
            <div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style="width:' + percent + '%"> \n\
                ' + percent_str + ' \n\
            </div>\n\
        </div>');
    order_element.append(progressElement);
}

function updatePositions(data) {
    var positionsElement = $("#positions");
    
    positionsElement.find("#position_quantity").remove()
    positionsElement.find("#position_amount").remove()
    
    var positionQuantity = $('<span id=position_quantity/>');
    positionQuantity.html('<b>Quantity:</b> ' + data.quantity);
    positionsElement.append(positionQuantity);
    
    var positionAmount = $('<span id=position_amount/>');
    positionAmount.html('<b>Amount:</b> ' + data.amount);
    positionsElement.append(positionAmount);
}

function removeOrderElement(order) {
    $("#orders").find(".order_" + order.id).remove();
}

function showOrderForm() {
    document.getElementById("addOrderForm").style.display = '';
}

function showRangeOrderForm() {
    document.getElementById("addOrderRangeForm").style.display = '';
}

function hideOBForm() {
    document.getElementById("addOrderForm").style.display = "none";
}

function hideOrderRangeForm() {
    document.getElementById("addOrderRangeForm").style.display = "none";
}

function removeAllNotPlaced() {
    var orderAction = {
        action: "removeanp"
    };
    socket.send(JSON.stringify(orderAction));
}

function formOBSubmit() {
    var form = document.getElementById("addOrderForm");
    var book = form.elements["order_book"].value;
    var side = form.elements["order_side"].value;
    var size = form.elements["order_size"].value;
    var price = form.elements["order_price"].value;
    hideOBForm();
    form.reset();
    addOrder(book, side, price, size);
}

function formOrderRangeSubmit() {
    var form = document.getElementById("addOrderRangeForm");
    var book = form.elements["order_book"].value;
    var side = form.elements["order_side"].value;
    var size = form.elements["order_size"].value;
    var start_price = form.elements["start_price"].value;
    var direction = form.elements["direction"].value;
    var levels = form.elements["levels"].value;
    var step = form.elements["step"].value;
    hideOrderRangeForm();
    form.reset();
    addOrderRange(book, side, start_price, step, size, levels, direction);
}

function init() {
    hideOBForm();
    hideOrderRangeForm();
}

function addOrderRange(book, side, start_price, step, size, levels, direction) {
    var orderAction = {
        action: "addor",
        book: book,
        side: side,
        price: start_price,
        step: step,
        size: size,
        levels: levels,
        direction: direction
    };
    socket.send(JSON.stringify(orderAction));
}

function addOrder(book, side, price, size) {
    var orderAction = {
        action: "addo",
        book: book,
        side: side,
        price: price,
        size: size
    };
    socket.send(JSON.stringify(orderAction));
}

function placeOrder(element) {
    var id = element;
    var OrderAction = {
        action: "place",
        id: id
    };
    socket.send(JSON.stringify(OrderAction));
}

function removeOrder(element) {
    var id = element;
    var OrderAction = {
        action: "remove",
        id: id
    };
    socket.send(JSON.stringify(OrderAction));
}

function cancelRemainingOrder(element) {
    var id = element;
    var OrderAction = {
        action: "cancel",
        id: id
    };
    socket.send(JSON.stringify(OrderAction));
}
