describe('check all', function(){

    var testContainer;
    var contactList;

    beforeEach(function() {
        contactList = new ContactList();
        testContainer = $('<div></div>');

        $('<input type="checkbox" id="all" />' +
        '<input type="checkbox" name="address" id="user1@gmail.com" />' +
        '<input type="checkbox" name="address" id="user2@gmail.com" />').appendTo(testContainer);

        testContainer.appendTo('body');
    })

    afterEach(function() {
        testContainer.remove();
    })

    it('click all checkbox', function() {
        contactList.checkAll();
        expect($('input[name="address"]:checked').length).toEqual(2);
    })


})