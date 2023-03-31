INSERT INTO subject (id, code, is_active, name)
VALUES  (1, 'MLN', true, 'Principles of Marxism - Leninism'),
        (2, 'PMG', true, 'Project management'),
        (3, 'PRU', true, 'Object-Oriented Programming for Unity Games'),
        (4, 'WDU', true, 'UI/UX Design'),
        (5, 'PRJ', true, 'Java Web Application Development'),
        (6, 'VNR', true, 'Revolutionary line of CPV'),
        (7, 'PRN', true, 'Basic Cross-Platform Application Programming With .NET'),
        (8, 'PRU', true, 'C# Programming and Unity'),
        (9, 'PRO', true, 'Object-Oriented Programming'),
        (10, 'MAT', true, 'Math');

INSERT INTO syllabus (id, code, description, is_active, is_syllabus_combo, min_avg_mark_to_pass, name, no_credit, no_slot, pre_requisite, reference_syllabus_in_combo, subject_id)
VALUES  (1, 'MLN111', 'Course introduction: Philosophy of Marxism – Leninism studies dialectical materialistic views on nature, society, and the mind, making the worldview of dialectical materialism becomes comprehensive. Applying and expanding the dialectical materialist perspective to social research, Marx has introduced historical materialism and pointed out the way to study the laws of social development and natural development governed by objective laws rather than subjective factors. The development of Marxism – Leninism philosophy has laid the foundation for the study of history and social life in a scientific way.', true, false, 5, 'Principles of Marxism - Leninism', 3, 36, NULL, NULL, 1),
        (2, 'MLN122', 'Course introduction: Political economics of Marxism – Leninism is an economic theory and scientific discipline on political economy developed by C. Marx, Engels and later Lenin in a new period, focusing on the capitalist mode of production and the production and economic exchange relations consistent with the capitalist mode of production, thereby clarifying the nature and phenomena of economic processes, laying a foundation to solve matters related to theories of Marxism - Leninism. The core of the Marxist-Leninist political economy is the surplus value theory of Marx.', true, false, 5, 'Political economics of Marxism – Leninism', 3, 36, NULL, NULL, 1),
        (3, 'VNR202', 'Course introduction: History of CPV is a major and a division of historical science. President Ho Chi Minh affirmed that the history of the Communist Party of Vietnam is golden pages. That is scientific, revolutionary and profound practical value in the Party''s platforms and guidelines, is proper leadership and timely response to requirements and missions set by the history; is the normative and theoretical experiences and lessons of Vietnamese revolution and the glorious traditions of the Party.
Researching and studying history of CPV not only requires mastering historical events and milestones but also understanding those issues in the process of leading and struggling; thereby applying and developing in the current period of comprehensive renovation and accelerating industrialization and modernization and international integration.', true, false, 5, 'Revolutionary line of CPV', 2, 30, '1,2', NULL, 6),
        (4, 'PRU211m', 'This course based on the C# Programming for Unity Game Development Specialization at https://www.coursera.org/specializations/programming-unity-game-development.
The course is all about starting to learn how to develop video games using the C# programming language and the Unity game engine on Windows or Mac. C# is a really good language for learning how to program and then programming professionally. Also, the Unity game engine is very popular with indie game developers; Unity games were downloaded 16,000,000,000 times in 2016! Finally, C# is one of the programming languages you can use in the Unity environment.', true, false, 5, 'C# Programming and Unity', 3, 45, '', NULL, 8),
        (5, 'PRU221m', 'This course is a course that builds on the knowledge included in the C# Programming for Unity Game Development Specialization, so the course assumes you have the prerequisite knowledge from the PRU211m. You should make sure you have that knowledge, either by taking those previous courses or from personal experience, before tackling this course. You should make sure you have that knowledge, either by taking those courses or from personal experience, before tackling this course. Throughout this course you''ll build on your foundational C# and Unity knowledge by developing more robust games with better object-oriented designs using file input and output, inheritance and polymorphism, event handling and various data structures and design patterns.
Data structures and design patterns are both general programming and software architecture topics that span all software, not just games. Although we''ll discuss these ideas in the game domain, they also apply if you''re writing a web app in ASP.NET, building a tool using WinForms, or any other software you decide to build.', true, false, 5, 'Object-Oriented Programming for Unity Games', 3, 45, '4', NULL, 8),
        (6, 'PRN211', 'Upon completion of this course students should:
1. Understand the followings:
• C# language for developing .NET applications;
• Fundamental concepts of .NET Platform
• Basic knowledge of Window Forms in .NET
• Basic knowledge of ASP.NET Core MVC
• Basic knowledge of RESTful API .NET
2. Be able to:
• Develop Cross-platform Desktop applications and support for user experience ( UI & UX )
• Develop Back-end applications by ASP.NET Core Web API
• Develop Distributed applications by Windows Forms and REST APIs
• Develop Distributed applications by ASP.NET MVC Core and REST APIs
3. Be able to work in a team and present group''s results', true, false, 5, 'Basic Cross-Platform Application Programming With .NET', 3, 45, NULL, NULL, 7),
        (7, 'PRN221', 'Upon completion of this course students should:
1. Understand the followings:
• Apply C# language for developing Desktop and Web applications;
• Fundamental concepts of .NET Core Platform
• Basic knowledge of Windows Presentation Foundation (WPF) and ASP.NET Core Razor Page application
• Basic knowledge of RESTful API .NET , Signal-R and apply into ASP.NET Core application
• Basic knowledge of Asynchronous and Parallel Programming in .NET Core application
• Basic knowledge of Dependency Injection apply into .NET Core applications
• Basic knowledge of Worker Service and apply to implement Background Tasks
2. Be able to:
• Develop Cross - platform Web applications by ASP.NET Core Razor Page
• Develop Cross - platform Desktop applications by WPF and support for user experience ( UI & UX )
• Implement Real-time applications by Signal R and ASP.NET Core
• Implement Background Tasks with Worker Service
3. Be able to work in a team and present group''s results', true, false, 5, 'Advanced Cross-Platform Application Programming With .NET', 3, 45, '6', NULL, 7),
        (8, 'PRN231', 'Upon completion of this course students shoud:
1. Understand the followings:
• Apply C# language for develop ASP.NET WEB API ( RESTful Service applications )
• Fundamental concepts of .NET Core Platform
• Basic knowledge of ASP.NET WEB API on .NET Core
• Basic knowledge of RESTful Service and Microservice architecture
2. Be able to:
• Develop Cross - platform Back-end application can be used by Desktop or Web applications (Cross - platform)
• Develop RESTful Service applications by ASP.NET Web API and Windows Communication Foundation (WCF)
• Implement security in the ASP.NET WEB API
• Implement sending Ajax request to ASP.NET WEB API
• Implement security JWT in ASP.NET Core Web API
• Develop Distributed applications based on Microservice architecture
3. Be able to work in team and present group''s results', true, false, 5, 'Building Cross-Platform Back-End Application With .NET', 3, 45, '7', NULL, 7),
        (46, 'SE_COM*1', 'Subject 1 of Combo', true, true, 5, 'Subject 1 of Combo', 3, 0, NULL, '6', NULL),
        (47, 'SE_COM*2', 'Subject 2 of Combo', true, true, 5, 'Subject 2 of Combo', 3, 0, NULL, '1,2,4,5,7', NULL),
        (48, 'SE_COM*3', 'Subject 3 of Combo', true, true, 5, 'Subject 3 of Combo', 2, 0, NULL, '3,8', NULL),
        (49, 'MAT101', 'djsakldjsakl;dasjkdl;as', true, false, 4, 'Syllabus 1111', 3, 30, NULL, NULL, 1),
        (50, 'SBT111', 'djsakldjsakl;dasjkdl;as', true, false, 4, 'Syllabus Test 1', 3, 30, '', '', 1);

INSERT INTO major (id, code, is_active, name)
VALUES  (1, 'IT', true, 'Information Technology'),
        (2, 'BA', true, 'Business Adminstrator');

INSERT INTO specialization (id, code, is_active, name, major_id)
VALUES  (1, 'SE', true, 'Software Engineering', 1),
        (2, 'GD', true, 'Digital Art & Design', 1),
        (3, 'IS', true, 'Information System', 1),
        (4, 'AI', true, 'Artificial Intelligence', 1),
        (5, 'MC', true, 'Multimedia Communication', 2),
        (6, 'MKT', true, 'Marketing', 2),
        (7, 'IB', true, 'International Business', 2),
        (8, 'TM', true, 'Tourism and Travel Management', 2);