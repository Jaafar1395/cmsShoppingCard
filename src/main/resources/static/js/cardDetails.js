$("span.checkout").click(function (e) {
    e.preventDefault();
    $("div.overlay").removeClass("d-none");
    $.get("/order/add", {}, function () {
    }).done(function (){
        $("form#paypalForm").submit();
    });
});

const euroEU = Intl.NumberFormat("en-DE", {
    style: "currency",
    currency: "EUR",
});

function updatePrices(id,data,incrementMode) {

    let productQuantity = data.productQuantity;
    let cardTotal = data.cardTotal;
    let productTotalPrice = data.productTotalPrice;

    let quantitySpan = $("." + id + "_quantity");

    quantitySpan.html(productQuantity);

    let subTotal = $("." + id + "_subTotal");
    subTotal.html(euroEU.format(productTotalPrice));

    let grandTotal = $(".grandTotal");
    grandTotal.html(euroEU.format(cardTotal));

    if (!incrementMode) {
        if (productQuantity === 0)
            quantitySpan.closest('tr').remove();
        if (cardTotal === 0)
            window.location.replace("/category/all");
    }
}

$("a.incrementQuantity").click(function (e) {
    e.preventDefault();
    let $this = $(this);
    let id = $this.attr("data-id");
    let url = "/card/json/add/" + id;
    $.get(url, {}, function (data) {
        updatePrices(id,data,true);
    });
});

$("a.decrementQuantity").click(function (e) {
    e.preventDefault();
    let $this = $(this);
    let id = $this.attr("data-id");
    let url = "/card/json/subtract/" + id;
    $.get(url, {}, function (data) {
        updatePrices(id,data,false);
    });
});

$("a.remove").click(function (e) {
    e.preventDefault();
    let $this = $(this);
    let id = $this.attr("data-id");
    let url = "/card/json/remove/" + id;
    $.get(url, {}, function (data) {
        let subTotal = $("." + id + "_subTotal");
        let cardTotal = data.cardTotal;
        let grandTotal = $(".grandTotal");
        subTotal.closest('tr').remove();

        if (cardTotal === 0)
            window.location.replace("/category/all");
        else
            grandTotal.html(euroEU.format(cardTotal));
    });
});