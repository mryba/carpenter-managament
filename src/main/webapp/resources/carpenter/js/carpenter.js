// Loader
function showLoader(data) {
    var loader = $('#loader-main');
    loader.show();
    if (data.status == 'success') {
        loader.hide();
    }
}

// Side panel
$(function() {
    $('.user-menu-btn').click(function() {
        $('.side-panel, .user-panel, .user-info, .user-menu-btn')
            .toggleClass('active');

    });
});

// Change side panel button icon
$(function() {
    $('.user-menu-btn').click(function() {
        $('#btn-panel').toggleClass('fa fa-bars fas fa-arrow-right');
    })
});
