function updateOwnerList(data) {
    ownerInput = document.getElementById("owner");

    for (var i = 0; i < data.length; i++) {
        option = document.createElement('option');
        option.value = data[i].OwnerID;
        option.innerHTML = data[i].OwnerID;
        ownerInput.appendChild(option);
    }
}

function getOwnerList() {
    getOwnerData("getOwnerList.php", updateOwnerList);
}

function getOwnerData(url, doSomething) {
    //doSomething("Test");
    var ajax = new XMLHttpRequest();

    ajax.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var data = JSON.parse(this.responseText);
            console.log(data);
            //doSomething (data);
            updateOwnerList(data);
        }
    }
    ajax.open("GET", "getOwnerList.php", true);
    ajax.send();
}
