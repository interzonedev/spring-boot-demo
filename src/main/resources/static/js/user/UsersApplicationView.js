(function($) {
    "use strict";

    iz.UsersApplicationView = Backbone.View.extend({

        el: $("#usersApplicationContainer"),

        currentView: null,

        render: function() {
            this.$el.append(this.currentView.$el);
            return this;
        },

        setView: function (view) {
            this.currentView = view;
            this.$el.empty();
            this.render();
        }
    });
})(jQuery);
