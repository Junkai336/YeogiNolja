$(document).ready(function () {
	$('input.timepickerCheckIn').timepicker({
            timeFormat: 'HH:mm',
            interval: 30,
            defaultTime: '11',
            startTime: '09:00',
            dynamic: false,
            dropdown: true,
            scrollbar: true
        });

        	$('input.timepickerCheckOut').timepicker({
                    timeFormat: 'HH:mm',
                    interval: 30,
                    defaultTime: '23',
                    startTime: '21:00',
                    dynamic: false,
                    dropdown: true,
                    scrollbar: true
                });

    })