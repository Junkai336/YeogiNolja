$(document).ready(function () {
	$('input.timepickerCheckIn').timepicker({
            timeFormat: 'HH:mm',
            interval: 30,
            defaultTime: '11',
            startTime: '11:00',
            dynamic: false,
            dropdown: true,
            scrollbar: true
        });

        	$('input.timepickerCheckOut').timepicker({
                    timeFormat: 'HH:mm',
                    interval: 30,
                    defaultTime: '13',
                    startTime: '13:00',
                    dynamic: false,
                    dropdown: true,
                    scrollbar: true
                });

    })