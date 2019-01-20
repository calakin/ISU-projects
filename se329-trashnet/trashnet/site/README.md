Here is a rough outline for the front end of the website so far. The form is not connected to anything so right now nothing will happen if you fill it out.  Also right now it's not fully
responsive so making it to small will break some of it. I will get that fix in the future.

AJAX
If you make your php file name "data.php" then you will not have to change anything in the ajax file.  If you do not name in data.php then in line 4 in the ajax.js you will have to change the name to
whatever you file is.

```javascript
ajax.open("GET", "data.php", true);
```