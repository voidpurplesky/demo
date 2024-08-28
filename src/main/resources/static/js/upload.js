//<!-- 7. 파일 업로드 처리 - 3. 컨트롤러와 화면처리 - 게시물과 첨부파일 등록 처리 p651-->
// Axios 처리를 위한 준비 p652
async function uploadToServer(formObj) {
    console.log("upload to server...");
    //console.log(formObj);
    const response = await axios({
        method: 'post',
        url:'/upload',
        data:formObj,
        headers: {'Content-Type': 'multipart/form-data',},

    });
    return response.data;
}

async function removeFileToServer(uuid, fileName) {
    const response = await axios.delete(`/remove/${uuid}_${fileName}`);
    return response.data;
}
