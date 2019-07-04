from time import sleep

import requests
import json
import pymysql

db = pymysql.connect(host="localhost", user="root", password="root", db="xshop", port=33061)


def getCursor():
    cursor = db.cursor()
    return cursor


class Classify(object):
    def __init__(self, **args):
        self.cl_name = args['cl_name']
        self.cl_grade = args['cl_grade'] is None if 0 else args['cl_grade']
        self.cl_fid = args['cl_fid']
        if 'picture' not in args:
            args['picture'] = ""
        self.picture = args['picture']

    def insert(self):
        sql = "insert into classify (cl_name,cl_grade,cl_fid,cl_serial,eid,picture) values (%s, %s, %s, %s, %s, %s)"
        cursor = getCursor()
        cursor.execute(sql, (self.cl_name, self.cl_grade, self.cl_fid, 0, 1, self.picture))
        cursor.close()
        db.commit()
        print("id=" + str(cursor.lastrowid))
        return cursor.lastrowid


def get_json(id):
    file = open('./res/shop_class' + str(id) + '.json')
    return json.loads(file.read())


def _downloadFile(url):
    global r
    if url is None or len(url) == 0:
        return None
    ss = url.split("/")
    name = ss[len(ss) - 1]
    r = _down(url, 0)
    if r is None:
        return ""
    with open('./res/shop_class_img/' + name, 'wb') as f:
        f.write(r.content)
    sleep(0.1)
    return name


def _down(url, time):
    try:
        return requests.get(url)
    except:
        sleep(5)
        print("重试中[" + str(time + 1) + "]....")
        # 超过三次不下载了
        if time + 1 == 3:
            return None
        return _down(url, time + 1)


def _json(id):
    json_arr = get_json(id)
    # 二级分类
    for item in json_arr['moduleList']:
        cl_id = Classify(cl_fid=id, cl_grade=1, cl_name=item['title']).insert()
        # 插入二级分类
        print('****************************' + item['title'] + '****************************')
        # 三级分类
        for i in item['items']:
            print(i['name'] + "  " + i['pic'])
            if i['name'] is None or i['name'] == "":
                continue
            if len(i['pic'].split(":")) < 2:
                print(i['pic'].split(":"))
                i['pic'] = "https:" + i['pic']
                print(i['pic'])

            img_name = _downloadFile(i['pic'])
            Classify(cl_fid=cl_id, cl_grade=2, cl_name=i['name'], picture=img_name).insert()
        print('****************************************************************')
        print('\n')


_json(1)
_json(2)
_json(3)
# print()
