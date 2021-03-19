(function(context) {
    "use strict";

    iz.User = Backbone.Model.extend({
        defaults: {
            id: null,
            firstName: null,
            lastName: null,
            email: null,
            timeCreated: null,
            timeUpdated: null
        },

        parse: function(response, options) {
            console.log("parse: response.user = " + JSON.stringify(response.user));
            this.set(response.user);
            return response.user;
        }
    });
}(this));
