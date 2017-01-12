# Instant-Book-Reviwer
An android application which app which can give real time reviews from internet by simply capturing a book’s cover.

## Why i develop it
When I am buying book (usually as a gift) I need to go through titles I never heard about. It's quite difficult to decide which one is good. Would be great if I can just take a picture of the cover with my mobile phone and it would instantly show me real time reviews from the readers. Could also show availability in different stores too. <br><br>
In case of this problem, <b>Book Judge</b>(Name of the app) can give the review of the scanned book real time from [Goodreads.com](https://www.goodreads.com/) which is well known site to book lovers.

## Platform
 - Android
 
## Minimun SDK version
 - 3.0 (HoneyComb, API 11)<br>
 
<b>N.B:</b> I run this app in <i><b>lolipop</b></i>(Android 5.0). But i don't know if it will work in <i><b>Marshmallow</b></i>(Android 6.0)

## Features
- Customized camera activity using surface view
- Get real time reviews by capturing image of book-cover
- Also get reviews by  typing book name
- Along with reviews user can get full description of the book provided by [Goodreads.com](https://www.goodreads.com/)
- Can add a book to read-list which user can use later for further purposes

## Used Libraries
- [Tesseract](https://github.com/rmtheis/tess-two)
- [Opencv](http://opencv.org/)

## Workflow
![workflow](http://i.imgur.com/xXlNk3l.png)


## App design
Home Page    |  Take photo using camera    | Getting XML data from Goodreads.com
:-------------------------:|:-------------------------:|:-------------------------:
![Home Page](http://i.imgur.com/GhM3ueq.jpg)  |  ![Take photo using camera](http://i.imgur.com/W3EqHzb.jpg)  |    ![Getting XML data from Goodreads.com](http://i.imgur.com/4PbYuJF.jpg)

</br>

Book Description   |  Reviews    | Buy Online
:-------------------------:|:-------------------------:|:-------------------------:
![Book Description](http://i.imgur.com/g9FGKtw.jpg)  |  ![Reviews](http://i.imgur.com/5tKEUN7.jpg)  |    ![Buy Online](http://i.imgur.com/W46YNOD.jpg)

</br>

Search by book title    |  ReadList
:-------------------------:|:-------------------------:
![Search by book title](http://i.imgur.com/49CUexa.jpg) |  ![ReadList](http://i.imgur.com/Qp55SI0.jpg)


## Limitations
- Only detect English language.
- Sometimes Google Tesseract’s performance is very poor.

