define([ 'lib/controller', 'hotel/list' ], function(Controller) {
	Controller.extend("SearchHotelsController", {
		
		template : 'hotel/search.html',
		delay : 1000,
		searching : null,

		init : function() {

			this.render();
			var self = this;
			

			$('#search-submit').bind('click', function() {
				var value = $('#search-value').val();
				$.storage.set('search-page', 0);
				$.publish('hotel-search', value);
			});

			$('#search-value').bind('keyup', function() {
				clearTimeout(self.searching);
				self.searching = setTimeout(function() {
					var value = $('#search-value').val();
					$.storage.set('search-page', 0);
					$.publish('hotel-search', value);
				}, self.delay);
			});

			$('#search-size').bind('change', function() {
				var value = $('#search-value').val();
				$.storage.set('search-page', 0);
				$.storage.set('search-size', $('#search-size').val());
				$.publish('hotel-search', value);
			});

			$('#search-size option[value=' + $('#search-size').val() + ']').attr('selected', 'selected');

			$('#result').list_hotels();

		}
	});
});
