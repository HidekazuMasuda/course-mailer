function ContactList() {}

ContactList.prototype.checkAll = function() {
    $('input[name="address"]').prop('checked', true);
}