(function(context) {
    "use strict";

    iz.UsersRouter = Backbone.Router.extend({
        applicationView: null,

        routes: {
            "": "showAll",
            "show/:id": "show",
            "create": "create",
            "update/:id": "update",
            "*path": "default"
        },

        initialize: function() {
            this.applicationView = new iz.UsersApplicationView();
        },

        routeToView: function(view) {
            this.applicationView.setView(view);
        },

        showAll: function() {
            this.routeToView(new iz.UsersView());
        },

        show: function(userId) {
            console.log("show: userId = " + userId);
            var user = new iz.User({
                id: userId
            });
            var view = new iz.UserView({
                model: user
            });
            this.routeToView(view);
        },

        create: function() {
            console.log("create");
        },

        update: function(userId) {
            console.log("update: userId = " + userId);
        },

        default: function(path) {
            console.log("default: path = " + path);
        }
    });

    $(function() {
        new iz.UsersRouter();
        Backbone.history.start();
    });
}(this));
