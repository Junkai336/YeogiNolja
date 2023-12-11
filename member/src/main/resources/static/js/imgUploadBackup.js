            $(document).ready(function() {
                            $('#summernote').summernote({
                                placeholder: '아이템 상세설명을 작성하세요.',
                                tabsize: 10,
                                height: 300,
                                toolbar: [
                                    // [groupName, [list of button]]
                                    ['fontname', ['fontname']],
                                    ['fontsize', ['fontsize']],
                                    ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
                                    ['color', ['forecolor','color']],
                                    ['table', ['table']],
                                    ['para', ['ul', 'ol', 'paragraph']],
                                    ['height', ['height']],
                                    ['insert',['picture','link','video']],
                                    ['view', ['fullscreen', 'help']]
                                ],
                                fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋움체','바탕체'],
                                fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72'],
                                maximumImageFileSize: 5000*1024, // 500 KB
                                callbacks: {
            onImageUpload: function (files, editor, welEditable) {
                for (var i = 0; i < files.length; i++) {
                    sendFile(files[i], this);
                }
            },

            onImageUploadError: function (msg) {
                window.alert(msg + "파일 최대 용량을 초과했습니다.");
            },

            onMediaDelete: function ($target, editor, $editable) {
                    var deletedImageUrl = $target
                        .attr('src')
                        .split('/')
                        .pop()

                    // ajax 함수 호출
                    console.log(deletedImageUrl);
                    deleteSummernoteImageFile(deletedImageUrl)
            },
                                }
                            });
                        });

     function sendFile(file, el) {
        var form_data = new FormData();
        form_data.append('file', file);

        var header = $("meta[name='_csrf_header']").attr('content');
        var token = $("meta[name='_csrf']").attr('content');

        $.ajax({
          data: form_data,
          type: "POST",
          url: '/image',

        beforeSend: function(xhr){
        xhr.setRequestHeader(header, token);
        },

          cache: false,
          contentType: false,
          enctype: 'multipart/form-data',
          processData: false,
          success: function(url) {
            $(el).summernote('editor.insertImage', url);
            $('#imageBoard > ul').append('<li><img src="'+url+'" width="480" height="auto"/></li>');
          }
        });
      }

     function deleteSummernoteImageFile(imageName) {
        data = new FormData()
        data.append('file', imageName)

        var header = $("meta[name='_csrf_header']").attr('content');
        var token = $("meta[name='_csrf']").attr('content');

            $.ajax({
             data: data,
             type: 'POST',
             url: '/imageDelete',

        beforeSend: function(xhr){
        xhr.setRequestHeader(header, token);
        },

             contentType: false,
             enctype: 'multipart/form-data',
             processData: false,
          })
     }


     function summernoteCall() {

     }