let suggestions = [];
$.getJSON("species.json", function(data) {
    $.each(data, function(key, value) {
        if ($.inArray(value.CommonName, suggestions) === -1) {
            suggestions.push(value.CommonName)
        }
    })
});

