// 6. Ajax와 json - 3. 댓글의 자바스크립트 처리 - 댓글 처리와 자바스크립트
// p572
async function get1(bno) {
    //console.log(bno);
    //console.log("PI: " + Math.PI);console.log(`PI: ${Math.PI}`);console.log("PI:", Math.PI);

    //const result = await axios.get(`/replies/list/`+bno);
    //console.log(result);

    const result = await axios.get(`/replies/list/${bno}`);
    return result.data;
}
// 댓글 목록 처리 p577
async function getList({bno, page, size, goLast}) {

    const result = await axios.get(`/replies/list/${bno}`, {params: {page, size}});
    // p582
    if (goLast) {
        const total = result.data.total;
        const lastPage = parseInt(Math.ceil(total/size));
        return getList({bno:bno, page:lastPage, size:size});
    }
    return result.data;
}
// 댓글등록 p584
async function addReply(replyObj) {
    const response = await axios.post(`/replies/`, replyObj);
    return response.data;
}
// 댓글조회와 수정 p588
async function getReply(rno) {
    const response = await axios.get(`/replies/${rno}`);
    return response.data;
}

async function modifyReply(replyObj) {
    const response = await axios.put(`/replies/${replyObj.rno}`, replyObj);
    return response.data;
}
// 댓글 삭제 p593
async function removeReply(rno) {
    const response = await axios.delete(`/replies/${rno}`);
    return response.data;
}