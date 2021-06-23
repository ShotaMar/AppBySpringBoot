"use strict";

let userData = null; //ユーザデータ
let table = null; //DataTablesオブジェクト

/** 画面ロード時の処理 */
jQuery(function ($) {
  //DataTablesの初期化
  createDataTables();

  /** 検索ボタンを押した時の処理 */
  $("#btn-search").click(function (event) {
    //検索
    search();
  });
});

/** 検索処理 */
function search() {
  //formの値を取得
  const formData = $("#user-search-form").serialize();

  //ajax
  $.ajax({
    type: "GET",
    url: "/user/get/list",
    data: formData,
    dataType: "json",
    contentType: "application/json; charset=UTF-8",
    cache: false,
    timeout: 5000,
  })
    .done(function (data) {
      //ajax成功時の処理
      console.log(data);
      //JSONを変数に入れる
      userData = data;
      //DataTables作成
      createDataTables();
    })
    .fail(function (jqXHR, textStatus, errorThrown) {
      //ajax失敗時の処理
      alert("検索処理に失敗しました");
    })
    .always(function () {
      //Do nothing
    });
}

function createDataTables() {
  //すでにDataTablesが作成されている場合
  if (table != null) {
    //DataTablesの破棄
    table.destroy();
  }

  //DataTablesの作成
  table = $("#user-list-table").DataTable({
    //日本語化
    language: {
      url: "/webjars/datatables-plugins/i18n/Japanese.json"
    },
    //表示データ
    data: userData,
    //データと列のマッピング
    columns: [
      { data: "userId" },
      { data: "userName" },
      {
        data: "birthday",
        render: function (data, type, row) {
          let date = new Date(data);
          let year = date.getFullYear();
          let month = date.getMonth() + 1;
          date = date.getDate();

          return `${year}/${month}/${date}`;
        },
      },
      { data: "age" },
      {
        data: "gender",
        render: function (data, type, row) {
          let gender = data === 1 ? "男性" : "女性";
          return gender;
        },
      },
      {
        data: "userId",
        render: function (data, type, row) {
          let url = `<a href="/user/detail/${data}">詳細</a>`;
          return url;
        },
      },
    ],
  });
}
