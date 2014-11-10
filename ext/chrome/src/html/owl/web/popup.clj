([:html
  [:head
   [:style
    "\n      * {\n        margin: 0px;\n        padding: 0px;\n      }\n\n      #vimiumPopup { width: 500px; }\n\n      #excludeControls {\n        padding: 10px;\n      }\n\n      #popupInput {\n        width: 330px;\n      }\n\n      #excludeConfirm {\n        display: inline-block;\n        width: 18px;\n        height: 13px;\n        background: url(../icons/check.png) 3px 2px no-repeat;\n        display: none;\n      }\n\n      #popupButton { margin-left: 10px; }\n\n      #popupMenu ul {\n        list-style: none;\n      }\n\n      #popupMenu a, #popupMenu a:active, #popupMenu a:visited {\n        color: #3F6EC2;\n        display: block;\n        border-top: 1px solid #DDDDDD;\n        padding: 3px;\n        padding-left: 10px;\n      }\n\n      #popupMenu a:hover {\n        background: #EEEEEE;\n      }\n\n      #optionsLink {\n        font-family : \"Helvetica Neue\", \"Helvetica\", \"Arial\", sans-serif;\n        font-size: 12px;\n      }\n    "]
   [:script {:src "js/main-dev.js"}]]
  [:body
   [:div#vimiumPopup
    [:div#excludeControls
     [:input#popupInput {:type "text"}]
     [:input#popupButton {:value "Exclude URL", :type "button"}]
     [:span#excludeConfirm "Saved."]]
    [:div#popupMenu
     [:ul [:li [:a#optionsLink {:target "_blank"} "Options"]]]]]]])
