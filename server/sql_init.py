import mysql.connector

if __name__ == "__main__":
    username = input()
    password = input()

    my_db = mysql.connector.connect(
        host="localhost",
        user=username,
        password=password
    )

    my_cursor = my_db.cursor()

    sql = """
        create database USER
    """
    my_cursor.execute(sql)

    my_db = mysql.connector.connect(
        host="localhost",
        user=username,
        password=password,
        database="USER"
    )

    my_cursor = my_db.cursor()

    sql = """
        create table Accounts
        (
            username varchar(128) not null,
            password varchar(128) not null
        )
    """
    my_cursor.execute(sql)

    sql = """
        create unique index Accounts_username_uindex
            on Accounts (username)
    """
    my_cursor.execute(sql)

    sql = """
        alter table Accounts
            add constraint Accounts_pk
                primary key (username)
    """
    my_cursor.execute(sql)

    sql = """
        insert into Accounts (username, password)
        values
            ('cuong', '1'),
            ('admin', 'admin'),
            ('user', 'pass')
    """
    my_cursor.execute(sql)

    sql = """
        create table Log
        (
            timestamp datetime not null,
            image varchar(128) not null,
            type varchar(128) not null
        )
    """
    my_cursor.execute(sql)

    sql = """
        alter table Log
        add
            id int PRIMARY KEY AUTO_INCREMENT
    """
    my_cursor.execute(sql)


    sql = """
        INSERT INTO Log(timestamp, image, type) VALUES
            ('2021-06-01 01:22:23','img_0.jpg','unknown'),
            ('2021-06-12 10:40:31','img_1.jpg','unknown')
    """
    my_cursor.execute(sql)
    my_db.commit()

    sql = """
    create table Schedule
        (
            username varchar(128) not null,
            time time not null,
            description varchar(128) not null,
            isActives int not null,
            toCautiousMode boolean not null,
            isEnabled boolean not null,
            constraint Schedule_pk
                primary key (username, time, isActives),
            constraint Schedule_Accounts_username_fk
                foreign key (username) references Accounts (username)
                    on update cascade on delete cascade
        );
    """
    my_cursor.execute(sql)

    sql = """
            INSERT INTO Schedule(username, time, description, isActives, toCautiousMode, isEnabled) VALUES
                ('admin','07:30:00','Off to work', 5, true, true),
                ('admin','16:30:00','Home', 127, false, false)
        """
    my_cursor.execute(sql)
    my_db.commit()
