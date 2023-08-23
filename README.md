# Virtual File System With Protection Layer

Virtual File System is a Java project that simulates the allocation and de-allocation of files and folders in a virtual file system using different allocation techniques. It provides a command-line interface for users to interact with the virtual file system and perform various operations such as creating files and folders, deleting files and folders, displaying disk status, and more. The project also includes a protection layer for user management and access control.

## Features

The Virtual File System project offers the following features:

- Contiguous Allocation using the Best Fit algorithm.
- Indexed Allocation.
- Linked Allocation.
- Creation and deletion of files and folders.
- Displaying disk status, including empty space, allocated space, empty blocks, and allocated blocks.
- Displaying the disk structure in a tree-like format.
- User management with the ability to create users and assign capabilities.
- Login functionality to switch between users with proper authentication.
- Command validation and error handling.
- File and folder permissions and access control.
- Ability to display file and folder details such as size, creation date, and last modified date.

## Commands

The Virtual File System project supports the following commands:

- `CreateFile <path> <size>`: Creates a file with the specified path and size in KB.
- `CreateFolder <path>`: Creates a new folder with the specified path.
- `DeleteFile <path>`: Deletes the file at the specified path and deallocates the blocks allocated to it.
- `DeleteFolder <path>`: Deletes the folder at the specified path, along with all its files and subdirectories.
- `DisplayDiskStatus`: Displays the status of the disk, including empty space, allocated space, empty blocks, and allocated blocks.
- `DisplayDiskStructure`: Displays the files and folders in the system in a tree structure.
- `Details <path>`: Displays the details of the file or folder at the specified path.
- `TellUser`: Displays the name of the currently logged-in user.
- `CUser <username> <password>`: Creates a new user with the specified username and password.
- `Grant <username> <folder> <capabilities>`: Grants the specified user create and delete capabilities on the specified folder.
- `Login <username> <password>`: Changes the currently logged-in user to the specified user.


## Getting Started

To run the Virtual File System project, follow these steps:

1. Clone the project repository from GitHub: [Virtual File System](https://github.com/KhaledAshrafH/Virtual-File-System).
2. Open the project in your preferred Java IDE.
3. Build the project to compile the source code.
4. Run the project.

## Usage

Once the project is running, you can enter commands to interact with the virtual file system. Below are examples of the available commands:
```
CreateFile /docs/report.txt 1024
CreateFolder /docs/notes
DeleteFile /docs/report.txt
DeleteFolder /docs/notes
DisplayDiskStatus
DisplayDiskStructure
TellUser
CUser khalouda pass123
Grant khalouda /docs create,delete
Login khalouda pass123
```

## Saving and Loading

The Virtual File System project supports saving and loading the virtual file system structure. When the application starts, it automatically loads the disk structure from the "c:\DiskStructure.vfs" file. Any changes made during the session are written back to the file before the application terminates.

The Virtual File System file contains the structure of the files and folders in a tree structure. This allows for printing the tree structure when the user requests the "DisplayDiskStructure" command.

## File Storage

The project uses two allocation techniques to store files in the disk: contiguous allocation (using best fit) and indexed allocation. The user can choose which technique to use by changing the value of the `allocationType` variable in the `Main` class.

### Contiguous Allocation

In contiguous allocation, each file occupies a set of contiguous blocks on the disk. The project uses the best fit strategy to find the smallest free space that can accommodate the file size. The project stores the start block number and the end block number for each file in the `DiskStructure.vfs` file.

### Indexed Allocation

In indexed allocation, each file has an index block that contains pointers to the data blocks of the file. The index block number is stored in the `DiskStructure.vfs` file, along with the data block numbers. The project uses a free space manager component to keep track of the free blocks and allocate them to files when needed.
## Extensibility

The Virtual File System project is designed to be extensible. Additional allocation techniques can be implemented by creating new classes that extend the `AllocationStrategy` abstract class and implementing the required methods. Moreover, new commands and functionality can be added by extending the existing classes and implementing the necessary logic.

## Contributing

Contributions to the Virtual File System project are welcome! If you find any issues or have suggestions for improvements, please open an issue on the project's GitHub repository. You can also fork the repository, make your changes, and submit a pull request.

## License

The Virtual File System project is licensed under the MIT License.
