import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FunctionalTestEditor implements ApplicationComponent, FileEditorProvider {

    public void initComponent() {
    }


    public void disposeComponent() {
    }

    public String getComponentName() {
        return "FunctionalTestEditor";
    }

    public boolean accept(Project project, VirtualFile file) {
        return file.getFileType() == StdFileTypes.XML;
    }

    public FileEditor createEditor(Project project, final VirtualFile file) {
        return new XMLEditor(project, file);
    }

    public void disposeEditor(FileEditor editor) {
    }

    @NotNull
    @Override
    public FileEditorState readState(@NotNull Element element, @NotNull Project project, @NotNull VirtualFile virtualFile) {
        return DummyFileEditorState.DUMMY;
    }

    public void writeState(FileEditorState state, Project project,

                           Element targetElement) {
    }

    @NonNls
    public String getEditorTypeId() {
        return getComponentName();
    }

    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.NONE;
    }

    private static class DummyFileEditorState implements FileEditorState {
        public static final FileEditorState DUMMY = new DummyFileEditorState();

        public boolean canBeMergedWith(FileEditorState otherState,
                                       FileEditorStateLevel level) {
            return false;
        }
    }

    private class XMLEditor implements FileEditor, FileEditorManagerListener {
        private final VirtualFile file;
        private final Project project;
        JPanel mainPanel;

        public XMLEditor(Project project, VirtualFile file) {
            this.file = file;
            this.project = project;
            mainPanel = new JPanel(new BorderLayout());


            String str = "Fail to read Content";
            try {

                byte[] bytes = file.contentsToByteArray();

                str = new String(bytes, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mainPanel.add(new Label("mon editeur de tests f : /n"+str));
            FileEditorManager.getInstance(project).addFileEditorManagerListener(this);
        }

        /**
         * @return component which represents editor in the UI.
         * <p>
         * The method should never return <code>null</code>.
         */
        public JComponent getComponent() {
            return mainPanel;
        }

        /**
         * Returns component to be focused when editor is opened. Method
         * <p>
         * should never return null.
         */
        public JComponent getPreferredFocusedComponent() {
            return mainPanel;
        }

        /**
         * @return editor's name, a string that identifies editor among
         * <p>
         * other editors. For example, UI form might have two
         * <p>
         * editor: "GUI Designer"
         * and "Text". So "GUI Designer" can be a name of one
         * <p>
         * editor and "Text"
         * can be a name of other editor. The method should never
         * <p>
         * return null]]>.
         */
        @NonNls
        public String getName() {
            return "vizualize functional test";
        }

        /**
         * @return editor's internal state. Method should never return
         * <p>
         * null]]>.
         */
        public FileEditorState getState(FileEditorStateLevel level) {
            return DummyFileEditorState.DUMMY;
        }

        /**
         * Applies given state to the editor.
         *
         * @param state cannot be null
         */
        public void setState(FileEditorState state) {
        }

        /**
         * @return whether the editor's content is modified in comparision
         * <p>
         * with its file.
         */
        public boolean isModified() {
            return false;
        }

        /**
         * @return whether the editor is valid or not. For some reasons
         * <p>
         * editor can become invalid. For example, text editor
         * <p>
         * becomes invalid when its file is deleted.
         */
        public boolean isValid() {
            return true;
        }

        /**
         * This method is invoked each time when the editor is selected.
         * <p>
         * This can happen in two cases: editor is selected because the
         * <p>
         * selected file
         * has been changed or editor for the selected file has been changed.
         */
        public void selectNotify() {
        }

        private void reloadContent() {
            FileDocumentManager fileDocManager =
                    FileDocumentManager.getInstance();
            Document doc = fileDocManager.getDocument(file);
            fileDocManager.saveDocument(doc);
        }

        /**
         * This method is invoked each time when the editor is deselected.
         */
        public void deselectNotify() {
        }

        /**
         * Removes specified listener
         *
         * @param listener to be added
         */
        public void addPropertyChangeListener(PropertyChangeListener listener) {
        }

        /**
         * Adds specified listener
         *
         * @param listener to be removed
         */
        public void removePropertyChangeListener(PropertyChangeListener
                                                         listener) {
        }

        /**
         * @return highlighter object to perform background analysis and
         * <p>
         * highlighting activities.
         * Return <code>null</code> if no background highlighting
         * <p>
         * activity necessary for this file editor.
         */
        public BackgroundEditorHighlighter getBackgroundHighlighter() {
            return null;
        }

        /**
         * The method is optional. Currently is used only by find usages
         * <p>
         * subsystem
         *
         * @return the location of user focus. Typically it's a caret or
         * <p>
         * any other form of selection start.
         */
        public FileEditorLocation getCurrentLocation() {
            return null;
        }

        public StructureViewBuilder getStructureViewBuilder() {
            return null;
        }


        /**
         * write javadoc
         */
        public void fileOpened(FileEditorManager source, VirtualFile file) {
        }

        /**
         * write javadoc
         */
        public void fileClosed(FileEditorManager source, VirtualFile file) {
        }

        /**
         *
         */
        public void selectionChanged(FileEditorManagerEvent event) {
            if (FileEditorManager.getInstance(project).getSelectedEditor(file) == this)
                reloadContent();
        }

        @Override
        public void dispose() {

        }

        @Nullable
        @Override
        public <T> T getUserData(@NotNull Key<T> key) {
            return null;
        }

        @Override
        public <T> void putUserData(@NotNull Key<T> key, @Nullable T t) {

        }
    }
}