$(function (){
    $(".confirmDeletion").click(function (){
       if (!confirm("Confirm deletion")) return false;
    });
});