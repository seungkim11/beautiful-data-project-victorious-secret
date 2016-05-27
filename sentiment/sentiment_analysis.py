from pymongo import MongoClient
from textblob import TextBlob

client = MongoClient('localhost', 27017)
db = client['reddit']
collection = db['posts_2016_04_23']
collection2 = db['posts_with_sentiment']

def parseComment(comment, count):
    blob = TextBlob(comment['body'])
    sentiment = blob.sentiment[0]
    comment['sentiment'] = sentiment
    count = count + 1

    if 'replies' in comment:
        replies = comment['replies']
        for reply in replies:
            count = parseComment(reply, count)

    return count



if __name__ == '__main__':
    cursor = collection.find()
    docCount = 0
    docs = []

    for doc in cursor:
        docCount = docCount + 1

        # for every 10000 documents insert
        if docCount % 10000 == 0:
            collection2.insert_many(docs)
            print 'inserted docs count: ' + str(docCount)
            del docs[:]

        count = 0

        if 'title' in doc:
            blob = TextBlob(doc['title'])
            doc['title_sentiment'] = blob.sentiment[0]

        if doc['comments']:
            comments = doc['comments']
            for comment in comments:
                count = parseComment(comment, count)

        doc['comments_count'] = count
        docs.append(doc)

    collection2.insert_many(docs)
    print 'inserted: ' + str(docCount) + ' docs'








