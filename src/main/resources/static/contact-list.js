function ContactList() {}

ContactList.prototype.checkAll = function() {
    var checked = $('#all').prop('checked');
    $('input[name="address"]').prop('checked', checked);
}