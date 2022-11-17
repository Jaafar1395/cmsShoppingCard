function addClearCardFunctionality(){
    $("#clearCard").click(function (){
        let url = "/card/clear?cardPage=true";
        $.get(url,{}, function (data) {
            $("div.card").html(data);
        });
    });
}

$("a.addToCard").click(function (e){
    e.preventDefault();
    let $this = $(this);
    $this.next().removeClass("d-none");
    let id = $this.attr("data-id");
    let url = "/card/add/" + id;
    $.get(url,{}, function (data) {
        $("div.card").html(data);
    }).done(function (){
        $this.parent().parent().find("div.productAdded").fadeIn();
        $this.next().addClass("d-none");
        setTimeout(() => {
            $this.parent().parent().find("div.productAdded").fadeOut();
        }, 1000);

        addClearCardFunctionality();
    });
});

addClearCardFunctionality();