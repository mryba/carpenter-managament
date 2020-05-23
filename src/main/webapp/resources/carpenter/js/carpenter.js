// Loader
function showLoader(data) {
    var loader = $('#loader-main');
    loader.show();
    if (data.status == 'success') {
        loader.hide();
    }
}
