package task5;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FolderProcessor extends RecursiveTask<List<String>> {
    private static final long serialVersionUID = 1L;
    private final String path;
    private final String extension;

    public FolderProcessor(String path, String extension) {
        this.path = path;
        this.extension = extension;
    }

    @Override
    protected List<String> compute() {
        List<String> namesOfStoredDirectoryFiles = new ArrayList<>();
        List<FolderProcessor> tasks = new ArrayList<>();
        File file = new File(path);
        File[] content = file.listFiles();

        if (content != null) {
            for (int i = 0; i < content.length; i++) {
                if (content[i].isDirectory()) {
                    FolderProcessor task = new FolderProcessor(content[i].getAbsolutePath(), extension);
                    task.fork();
                    tasks.add(task);
                } else {
                    if (checkFile(content[i].getName())) {
                        namesOfStoredDirectoryFiles.add(content[i].getPath());
                    }
                }
            }
        }

        System.out.printf("%s: %d tasks ran.\n", file.getAbsolutePath(), namesOfStoredDirectoryFiles.size());

        addResultsFromTasks(namesOfStoredDirectoryFiles, tasks);
        return namesOfStoredDirectoryFiles;
    }

    private void addResultsFromTasks(List<String> list, List<FolderProcessor> tasks) {
        for (FolderProcessor item : tasks) {
            list.addAll(item.join());
        }
    }

    private boolean checkFile(String name) {
        return name.endsWith(extension);
    }
}