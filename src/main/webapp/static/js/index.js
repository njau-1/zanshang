/**
 * Created by Lookis on 8/17/15.
 */

$(function () {
    loadImg();
    var video = $('video').first();
    video.on("ended", function () {
        window.location = "/projects/55fea522e4b0039f31404935";
    });
    $('video').each(function (index, elm) {
        elm.onclick = function () {
            if (elm.paused) {
                elm.play();
            } else {
                elm.pause();
            }
            return false;
        }
    });
})