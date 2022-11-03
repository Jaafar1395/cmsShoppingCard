$(function (){
    $(".confirmDeletion").click(function (){
       if (!confirm("Confirm deletion")) return false;
    });
});

function readUrl(input){

    if(input.files && input.files[0]){
        let reader = new FileReader();
        reader.onload = function (event){
            $("#imgPreview").attr("src", event.target.result).width(100).height(100);
        }

        reader.readAsDataURL(input.files[0]);

    }
}