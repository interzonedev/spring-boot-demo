(function(context) {
    "use strict";

    iz.Users = Backbone.Collection.extend({
        url: '/user',
        model: iz.User,

        parse: function(response, options) {
            var _this = this;

            _.each(response.users, function(item, index) {
                var user = new _this.model();
                user.set(item);

                _this.push(user);
            });
            return this.models;
        }
    });
}(this));
