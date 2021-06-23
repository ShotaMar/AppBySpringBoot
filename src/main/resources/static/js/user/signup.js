'use strict'

jQuery(function($){
    $('#btn-signup').click(function(event){
        signupUser()
    })
})

function signupUser(){
    removeValidResult()
    const formData = $('#signup-form').serializeArray()

    $.ajax({
        type: 'POST',
        chache: false,
        url: '/user/signup/rest',
        data: formData,
        dataType: 'json'

    }).done(function(data){
        console.log(data)

        if(data.result === 90){
            $.each(data.errors, function(key,value){
                reflectValidResult(key,value)
            })
        
        }else if(data.result === 0){
            alert('ユーザ登録をしました')
            window.location.href='/login'
        }

    }).fail(function(jqXHR,textStatus,errorThrown){
        alert('ユーザ登録に失敗しました')
    
    }).always(function(){
        //Do nothing
    })
}

function removeValidResult(){
    $('.is-invalid').removeClass('is-invalid')
    $('.invalid-feedback').remove()
    $('.text-danger').remove()
}

function reflectValidResult(key,value){
    if(key === 'gender'){
        $(`input[name=${key}]`).addClass('is-invalid')
        $(`input[name=${key}]`)
            .parent()
            .parent()
            .append(`<div class="text-danger">${value}</div>`)
    
    }else{
        $(`input[id=${key}]`).addClass('is-invalid')
        $(`input[id=${key}]`).after(`<div class="invalid-feedback">${value}</div>`)
    }
}





// // 'user strict'
// /** 画面ロード時の処理 */
// window.addEventListener('DOMContentLoaded',()=>{
//     console.log('domcontentloaded')

//     /** 登録ボタンを押した時の処理 */
//     document.getElementById('btn-signup').addEventListener('click',(event)=>{
//         console.log('onclick!!!')
//         //ユーザ登録
//         signupUser()
//             .then(data =>{
//                 console.log(JSON.stringify(data))
                
//                 if(data.result === 90){
//                     data.errors.forEach((key,value) => {
//                         reflectValidResult(key,value)
//                     })
//                 }else if(data.result === 0){
//                     alert('ユーザを登録しました')
//                     window.location.href = '/login'
//                 }
//             })
//             .catch((xhr,textStatus,errorThrown) =>{
//                 alert('ユーザ登録に失敗しました')
//                 console.log(xhr.jsonStringif)

//                 console.log(textStatus)
//                 console.log(errorThrown)

//             })
        
//     })
// })

// async function signupUser(){
//     console.log('signupuser execute')
//      //バリデーション結果をクリア
//     removeValidResult()
//      //フォームの値を取得
//     let formData = document.getElementById('signup-form').value
//      //ユーザ登録
//     const response = await fetch('/user/signup/rest')
//     const data = await response.json()
//     console.log(data)

//     return data
// }

// /** バリデーション結果をクリア */
// function removeValidResult(){
//     document.querySelector('.is-invalid').remove()
//     document.querySelector('.invalid-feedback').remove()
//     document.querySelector('.text-danger').remove()
// }

// /** バリデーション結果の反映 */
// function reflectValidResult(key,value){
//     let inputKey = document.querySelector(`input[id=${key}]`)
//     if(key === 'gender'){
//         inputKey.parent()
//             .parent()
//             .append(`<div class="text-danger">${value}</div>`)
//     }else{
//         inputKey.addClass('is-invalid')
//         inputKey.after(`<div class="invalid-feedback">${value}</div>`)
//     }
// }