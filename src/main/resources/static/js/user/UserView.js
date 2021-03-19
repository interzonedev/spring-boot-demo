(function($) {
    "use strict";

    iz.UserView = Backbone.View.extend({

        tagName : "div",

        initialize : function() {
            // every function that uses "this" as the current object should be in here
            _.bindAll(this, "render", "displayUser");

            this.model.fetch({
                url: '/user/' + this.model.get("id"),
                type: 'get',
                contentType: 'application/json',
                reset: true,
                error: function(theModel, response, options) {
                    console.log("fetch.error: response.status = " + response.status);
                    console.log("fetch.error: response.responseText = " + response.responseText);
                }
            });

            this.model.bind("change", this.displayUser);

            this.render();
        },

        render: function() {
            $(this.el).append("<div></div>");
            $(this.el).append("<a href='#'>All Users</a>");
            return this;
        },

        displayUser: function(user) {
            $("div", this.el).append(
                "<span>" + this.model.get("id") + " - " + this.model.get("firstName") + " " + this.model.get("lastName") + "</span>"
            );
        }
    });
})(jQuery);
