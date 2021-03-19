(function($) {
    "use strict";

    iz.UsersView = Backbone.View.extend({
        tagName: "div",

        initialize: function() {
            // every function that uses "this" as the current object should be in here
            _.bindAll(this, "render", "appendUser");

            this.users = new iz.Users();
            this.users.fetch({
                url: this.users.url,
                type: 'get',
                contentType: 'application/json',
                reset: true,
                error: function(theCollection, response, options) {
                    console.log("fetch.error: response.status = " + response.status);
                    console.log("fetch.error: response.responseText = " + response.responseText);
                }
            });
            this.users.bind("add", this.appendUser);

            this.render();
        },

        render: function() {
            var self = this;
            $(this.el).append("<a href='#create'>Add User</a>");
            $(this.el).append("<ul></ul>");
            _(this.users.models).each(function(user) {
                self.appendUser(user);
            }, this);
            return this;
        },

        appendUser: function(user) {
            $("ul", this.el).append(
                "<li><span>" + user.get("firstName") + " " + user.get("lastName") + "</span>" +
                "&nbsp;&nbsp;" +
                "<a class='viewUser' href='#show/" + user.get("id") + "'>[view]</a>" +
                "&nbsp;&nbsp;" +
                "<a class='editUser' href='#update/" + user.get("id") + "'>[edit]</a></li>"
            );
        }
    });
})(jQuery);
